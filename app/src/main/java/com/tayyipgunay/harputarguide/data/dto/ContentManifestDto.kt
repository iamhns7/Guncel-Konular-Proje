package com.tayyipgunay.harputarguide.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ContentManifestDto(
    val version: Int = 1,
    val defaultLocale: String? = null,
    val supportedLocales: List<String> = emptyList(),
    val mediaBasePath: String? = null
)
