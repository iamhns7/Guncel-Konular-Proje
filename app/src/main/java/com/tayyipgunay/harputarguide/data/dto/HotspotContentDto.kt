package com.tayyipgunay.harputarguide.data.dto

import com.tayyipgunay.harputarguide.data.ar.HotspotContentPaths
import com.tayyipgunay.harputarguide.domain.model.HotspotActions
import com.tayyipgunay.harputarguide.domain.model.HotspotContentSections
import com.tayyipgunay.harputarguide.domain.model.HotspotDetailContent
import com.tayyipgunay.harputarguide.domain.model.HotspotFeature
import com.tayyipgunay.harputarguide.domain.model.HotspotMedia
import kotlinx.serialization.Serializable

@Serializable
data class HotspotContentPlaceDto(
    val placeId: String,
    val title: String = "",
    val purpose: String? = null,
    val hotspots: List<HotspotDetailContentDto> = emptyList()
)

@Serializable
data class HotspotDetailContentDto(
    val id: String,
    val name: String,
    val contentType: String? = null,
    val shortDescription: String? = null,
    val detailDescription: String? = null,
    val features: List<HotspotFeatureDto>? = null,
    val sections: HotspotContentSectionsDto? = null,
    val actions: HotspotActionsDto? = null,
    val media: HotspotMediaDto? = null,
    val audioText: String? = null,
    val historyIdea: String? = null
)

@Serializable
data class HotspotContentSectionsDto(
    val material: String? = null,
    val construction: String? = null,
    val purpose: String? = null,
    val significance: String? = null
)

@Serializable
data class HotspotFeatureDto(
    val label: String,
    val value: String
)

@Serializable
data class HotspotActionsDto(
    val show3d: Boolean = false,
    val showHistory: Boolean = false,
    val showAudio: Boolean = false,
    val showMaterialAnalysis: Boolean = false
)

@Serializable
data class HotspotMediaDto(
    val detailImage: String? = null,
    val model3d: String? = null
)

fun HotspotDetailContentDto.toDomain(): HotspotDetailContent = HotspotDetailContent(
    id = id,
    name = name,
    contentType = contentType,
    shortDescription = shortDescription.orEmpty(),
    detailDescription = detailDescription.orEmpty(),
    sections = HotspotContentSections(
        material = sections?.material.orEmpty(),
        construction = sections?.construction.orEmpty(),
        purpose = sections?.purpose.orEmpty(),
        significance = sections?.significance.orEmpty()
    ),
    features = features?.map { HotspotFeature(it.label, it.value) }.orEmpty(),
    actions = HotspotActions(
        show3d = actions?.show3d == true,
        showHistory = actions?.showHistory == true,
        showAudio = actions?.showAudio == true,
        showMaterialAnalysis = actions?.showMaterialAnalysis == true
    ),
    media = HotspotMedia(
        detailImageAssetPath = media?.detailImage?.let { HotspotContentPaths.resolveAssetPath(it) },
        model3dAssetPath = media?.model3d?.let { HotspotContentPaths.resolveAssetPath(it) }
    ),
    audioText = audioText,
    historyIdea = historyIdea
)
