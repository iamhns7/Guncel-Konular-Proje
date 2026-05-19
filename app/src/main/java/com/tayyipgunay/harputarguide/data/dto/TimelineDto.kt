package com.tayyipgunay.harputarguide.data.dto

import kotlinx.serialization.Serializable

/**
 * Timeline meta JSON — fields vary by file (e.g. [description] vs [year] for zindan-kesfi).
 */
@Serializable
data class TimelineMetaDto(
    val id: String,
    val placeId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val year: String? = null,
    val imageAsset: String? = null
)

@Serializable
data class TimelineStoryDto(
    val id: String,
    val placeId: String? = null,
    val events: List<TimelineEventDto> = emptyList()
)

@Serializable
data class TimelineEventDto(
    val id: String,
    val year: String? = null,
    val title: String? = null,
    val content: String? = null,
    val imageAsset: String? = null
)
