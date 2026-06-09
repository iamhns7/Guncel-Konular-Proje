package com.tayyipgunay.harputarguide.domain.model

import com.tayyipgunay.harputarguide.data.asset.ConstructionStep
import com.tayyipgunay.harputarguide.data.asset.HotspotDetailData
import com.tayyipgunay.harputarguide.data.asset.HotspotProperty
import com.tayyipgunay.harputarguide.data.asset.MaterialInfoRow
import com.tayyipgunay.harputarguide.data.asset.MaterialLayerLabel
import com.tayyipgunay.harputarguide.data.asset.StructureHotspot

data class ArPlaceInfo(
    val constructionPeriod: String?,
    val materialAndArchitecture: String?,
    val historySliderIdea: String?
)

data class ArInfoContent(
    val title: String,
    val location: String,
    val description: String
)

data class ArInscriptionContent(
    val textTr: String,
    val textEn: String
)

data class ArAudioContent(
    val scriptTr: String,
    val scriptEn: String
)

data class ArPlaceContent(
    val placeId: String,
    val title: String,
    val shortDescription: String,
    val referenceImageAssetPath: String,
    val physicalWidthMeters: Float,
    val hotspots: List<StructureHotspot>,
    val hotspotDetails: Map<String, HotspotDetailData>,
    val placeInfo: ArPlaceInfo?,
    val info: ArInfoContent?,
    val pastImageAssetPath: String?,
    val inscription: ArInscriptionContent?,
    val audio: ArAudioContent?
) {
    fun getHotspotDetail(hotspotId: String): HotspotDetailData? = hotspotDetails[hotspotId]

    fun toStructureHotspots(): List<StructureHotspot> = hotspots
}

