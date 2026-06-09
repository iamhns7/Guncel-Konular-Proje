package com.tayyipgunay.harputarguide.domain.model

data class HotspotContentPlace(
    val placeId: String,
    val title: String,
    val purpose: String?,
    val hotspots: List<HotspotDetailContent>
)

data class HotspotDetailContent(
    val id: String,
    val name: String,
    val contentType: String?,
    val shortDescription: String,
    val detailDescription: String,
    val sections: HotspotContentSections,
    val features: List<HotspotFeature>,
    val actions: HotspotActions,
    val media: HotspotMedia,
    val audioText: String?,
    val historyIdea: String?
)

data class HotspotContentSections(
    val material: String = "",
    val construction: String = "",
    val purpose: String = "",
    val significance: String = ""
) {
    fun hasStructuredContent(): Boolean =
        material.isNotBlank() ||
            construction.isNotBlank() ||
            purpose.isNotBlank() ||
            significance.isNotBlank()
}

data class HotspotFeature(
    val label: String,
    val value: String
)

data class HotspotActions(
    val show3d: Boolean,
    val showHistory: Boolean,
    val showAudio: Boolean,
    val showMaterialAnalysis: Boolean
)

data class HotspotMedia(
    val detailImageAssetPath: String?,
    val model3dAssetPath: String?
)
