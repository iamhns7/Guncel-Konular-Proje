package com.tayyipgunay.harputarguide.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlaceCoordinateDto(
    val latitude: Double? = null,
    val longitude: Double? = null
)

@Serializable
data class PlaceMetaDto(
    val id: String,
    val name: String,
    val shortDescription: String? = null,
    val imageAsset: String? = null,
    val category: String? = null,
    val coordinate: PlaceCoordinateDto? = null,
    val yearOrEra: String? = null
)
