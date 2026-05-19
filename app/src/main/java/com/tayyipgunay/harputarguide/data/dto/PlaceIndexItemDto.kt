package com.tayyipgunay.harputarguide.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlaceIndexItemDto(
    val id: String,
    val name: String,
    val imageFile: String? = null,
    val yearOrEra: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
