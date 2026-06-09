package com.tayyipgunay.harputarguide.data.repository

import android.content.Context
import com.tayyipgunay.harputarguide.core.common.AppLogger
import com.tayyipgunay.harputarguide.data.ar.HotspotContentPaths
import com.tayyipgunay.harputarguide.data.content.AssetJsonReader
import com.tayyipgunay.harputarguide.data.dto.HotspotContentPlaceDto
import com.tayyipgunay.harputarguide.data.dto.toDomain
import com.tayyipgunay.harputarguide.domain.model.HotspotDetailContent
import com.tayyipgunay.harputarguide.domain.repository.HotspotContentRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HotspotContentRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : HotspotContentRepository {

    private val reader = AssetJsonReader(context)
    private val placeCache = mutableMapOf<String, HotspotContentPlaceDto>()

    override suspend fun getHotspotContent(
        placeId: String,
        hotspotId: String
    ): HotspotDetailContent? = withContext(Dispatchers.IO) {
        val place = loadPlace(placeId) ?: return@withContext null
        val content = place.hotspots.find { it.id == hotspotId }?.toDomain() ?: return@withContext null
        val safeDetailImage = content.media.detailImageAssetPath?.takeIf { assetExists(it) }
        val safeModel = content.media.model3dAssetPath?.takeIf { assetExists(it) }
        content.copy(
            media = content.media.copy(
                detailImageAssetPath = safeDetailImage,
                model3dAssetPath = safeModel
            )
        ).also {
            AppLogger.d("Hotspot içerik yüklendi: $placeId/$hotspotId")
        }
    }

    override fun assetExists(assetPath: String?): Boolean {
        if (assetPath.isNullOrBlank()) return false
        return reader.assetExists(assetPath)
    }

    private fun loadPlace(placeId: String): HotspotContentPlaceDto? {
        placeCache[placeId]?.let { return it }
        val path = HotspotContentPaths.placePath(placeId)
        val place = reader.decode<HotspotContentPlaceDto>(path)
            .onFailure { AppLogger.e("Hotspot içerik okunamadı ($path).", it) }
            .getOrNull()
        if (place != null) {
            placeCache[placeId] = place
        }
        return place
    }
}
