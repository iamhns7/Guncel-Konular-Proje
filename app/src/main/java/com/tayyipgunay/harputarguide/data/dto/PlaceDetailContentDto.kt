package com.tayyipgunay.harputarguide.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlaceDetailContentDto(
    val id: String,
    val about: String? = null,
    val audioAsset: String? = null,
    val modelAsset: String? = null
)
