package com.tayyipgunay.harputarguide.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.tayyipgunay.harputarguide.core.common.AppLogger
import com.tayyipgunay.harputarguide.data.ar.ArAssetPaths
import com.tayyipgunay.harputarguide.data.asset.ConstructionStep
import com.tayyipgunay.harputarguide.data.asset.HotspotDetailData
import com.tayyipgunay.harputarguide.data.asset.HotspotProperty
import com.tayyipgunay.harputarguide.data.asset.MaterialInfoRow
import com.tayyipgunay.harputarguide.data.asset.MaterialLayerLabel
import com.tayyipgunay.harputarguide.data.asset.StructureHotspot
import com.tayyipgunay.harputarguide.data.content.AssetJsonReader
import com.tayyipgunay.harputarguide.data.dto.ArHotspotDto
import com.tayyipgunay.harputarguide.data.dto.ArManifestDto
import com.tayyipgunay.harputarguide.data.dto.ArPlaceContentDto
import com.tayyipgunay.harputarguide.data.dto.ArPlaceInfoDto
import com.tayyipgunay.harputarguide.domain.model.ArAudioContent
import com.tayyipgunay.harputarguide.domain.model.ArInfoContent
import com.tayyipgunay.harputarguide.domain.model.ArInscriptionContent
import com.tayyipgunay.harputarguide.domain.model.ArPlaceContent
import com.tayyipgunay.harputarguide.domain.model.ArPlaceInfo
import com.tayyipgunay.harputarguide.domain.repository.ArContentRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArContentRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ArContentRepository {

    private val reader = AssetJsonReader(context)
    private var manifestCache: ArManifestDto? = null
    private val contentCache = mutableMapOf<String, ArPlaceContent>()

    override suspend fun getSupportedPlaceIds(): List<String> = withContext(Dispatchers.IO) {
        loadManifest().supportedPlaceIds
    }

    override suspend fun isArSupported(placeId: String): Boolean = withContext(Dispatchers.IO) {
        loadManifest().supportedPlaceIds.contains(placeId)
    }

    override suspend fun getPlaceContent(placeId: String): ArPlaceContent? = withContext(Dispatchers.IO) {
        contentCache[placeId]?.let { return@withContext it }
        val dto = reader.decode<ArPlaceContentDto>(ArAssetPaths.placeContentPath(placeId))
            .onFailure { AppLogger.e("AR içerik okunamadı (placeId=$placeId).", it) }
            .getOrNull() ?: return@withContext null

        dto.toDomain().also { contentCache[placeId] = it }
    }

    override suspend fun loadReferenceBitmap(placeId: String): Bitmap? = withContext(Dispatchers.IO) {
        val content = getPlaceContent(placeId) ?: return@withContext null
        runCatching {
            context.assets.open(content.referenceImageAssetPath).use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        }.onFailure {
            AppLogger.e("AR referans görseli yüklenemedi (placeId=$placeId).", it)
        }.getOrNull()
    }

    override suspend fun getHotspots(placeId: String): List<StructureHotspot> =
        getPlaceContent(placeId)?.hotspots.orEmpty()

    override suspend fun getHotspotDetail(placeId: String, hotspotId: String): HotspotDetailData? =
        getPlaceContent(placeId)?.getHotspotDetail(hotspotId)

    private fun loadManifest(): ArManifestDto {
        manifestCache?.let { return it }
        val manifest = reader.decode<ArManifestDto>(ArAssetPaths.MANIFEST)
            .onFailure { AppLogger.e("AR manifest okunamadı.", it) }
            .getOrElse { ArManifestDto() }
        manifestCache = manifest
        return manifest
    }

    private fun ArPlaceContentDto.toDomain(): ArPlaceContent {
        val structureHotspots = hotspots.map { it.toStructureHotspot() }
        val details = hotspots.associate { hotspot ->
            hotspot.id to hotspot.toHotspotDetail()
        }
        val placeInfo = info?.toDomain()
        val legacyModes = modes

        val infoContent = legacyModes?.info?.let {
            ArInfoContent(
                title = it.title,
                location = it.location,
                description = it.description
            )
        } ?: ArInfoContent(
            title = title.ifBlank { placeId },
            location = "Harput, Elazığ",
            description = buildInfoDescription(shortDescription, info)
        )

        return ArPlaceContent(
            placeId = placeId,
            title = title.ifBlank { infoContent.title },
            shortDescription = shortDescription,
            referenceImageAssetPath = resolveReferenceImagePath(referenceImage),
            physicalWidthMeters = physicalWidthMeters,
            hotspots = structureHotspots,
            hotspotDetails = details,
            placeInfo = placeInfo,
            info = infoContent,
            pastImageAssetPath = legacyModes?.past?.pastImage,
            inscription = legacyModes?.inscription?.let {
                ArInscriptionContent(textTr = it.textTr, textEn = it.textEn)
            },
            audio = legacyModes?.audio?.let {
                ArAudioContent(scriptTr = it.scriptTr, scriptEn = it.scriptEn)
            }
        )
    }

    private fun buildInfoDescription(shortDescription: String, info: ArPlaceInfoDto?): String {
        if (info == null) return shortDescription
        return listOfNotNull(
            shortDescription.takeIf { it.isNotBlank() },
            info.constructionPeriod?.takeIf { it.isNotBlank() },
            info.materialAndArchitecture?.takeIf { it.isNotBlank() }
        ).joinToString("\n\n")
    }

    private fun resolveReferenceImagePath(referenceImage: String): String = when {
        referenceImage.startsWith("${ArAssetPaths.ROOT}/") -> referenceImage
        referenceImage.startsWith("images/") -> "${ArAssetPaths.ROOT}/$referenceImage"
        else -> "${ArAssetPaths.ROOT}/images/$referenceImage"
    }

    private fun ArPlaceInfoDto.toDomain(): ArPlaceInfo = ArPlaceInfo(
        constructionPeriod = constructionPeriod,
        materialAndArchitecture = materialAndArchitecture,
        historySliderIdea = historySliderIdea
    )

    private fun ArHotspotDto.toStructureHotspot(): StructureHotspot =
        StructureHotspot(
            id = id,
            name = name,
            shortDescription = shortDescription,
            x = x,
            y = y,
            subtitle = subtitle
        )

    private fun ArHotspotDto.toHotspotDetail(): HotspotDetailData {
        val detailData = detail
        return HotspotDetailData(
            hotspotId = id,
            name = name,
            longDescription = detailData?.longDescription?.takeIf { it.isNotBlank() } ?: shortDescription,
            properties = detailData?.properties?.map { HotspotProperty(it.label, it.value) }.orEmpty(),
            constructionSteps = detailData?.constructionSteps?.map {
                ConstructionStep(it.order, it.title, it.description)
            }.orEmpty(),
            materialRows = detailData?.materialRows?.map { MaterialInfoRow(it.label, it.value) }.orEmpty(),
            materialLayers = detailData?.materialLayers?.map { MaterialLayerLabel(it.title, it.yRatio) }.orEmpty()
        )
    }
}

