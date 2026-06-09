package com.tayyipgunay.harputarguide.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArManifestDto(
    val version: Int = 1,
    val supportedPlaceIds: List<String> = emptyList()
)

@Serializable
data class ArPlaceContentDto(
    val placeId: String,
    val title: String = "",
    val referenceImage: String,
    val physicalWidthMeters: Float,
    val shortDescription: String = "",
    val info: ArPlaceInfoDto? = null,
    val hotspots: List<ArHotspotDto> = emptyList(),
    val modes: ArModesDto? = null
)

@Serializable
data class ArPlaceInfoDto(
    val constructionPeriod: String? = null,
    val materialAndArchitecture: String? = null,
    val historySliderIdea: String? = null
)

@Serializable
data class ArHotspotDto(
    val id: String,
    val name: String,
    val x: Float,
    val y: Float,
    val shortDescription: String,
    val subtitle: String? = null,
    val detail: ArHotspotDetailDto? = null
)

@Serializable
data class ArHotspotDetailDto(
    val longDescription: String = "",
    val properties: List<ArKeyValueDto> = emptyList(),
    val constructionSteps: List<ArConstructionStepDto> = emptyList(),
    val materialRows: List<ArKeyValueDto> = emptyList(),
    val materialLayers: List<ArMaterialLayerDto> = emptyList()
)

@Serializable
data class ArKeyValueDto(
    val label: String,
    val value: String
)

@Serializable
data class ArConstructionStepDto(
    val order: Int,
    val title: String,
    val description: String
)

@Serializable
data class ArMaterialLayerDto(
    val title: String,
    val yRatio: Float
)

@Serializable
data class ArModesDto(
    val info: ArInfoModeDto? = null,
    val past: ArPastModeDto? = null,
    val inscription: ArInscriptionModeDto? = null,
    val audio: ArAudioModeDto? = null
)

@Serializable
data class ArInfoModeDto(
    val title: String,
    val location: String,
    val description: String
)

@Serializable
data class ArPastModeDto(
    val pastImage: String? = null
)

@Serializable
data class ArInscriptionModeDto(
    val textTr: String = "",
    val textEn: String = ""
)

@Serializable
data class ArAudioModeDto(
    val scriptTr: String = "",
    val scriptEn: String = ""
)

