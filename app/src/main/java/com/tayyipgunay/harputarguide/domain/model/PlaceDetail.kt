package com.tayyipgunay.harputarguide.domain.model

data class PlaceDetail(
    val id: String,
    val name: String,
    val location: String,
    val description: String,
    val period: String,
    val estimatedBuild: String,
    val category: String,
    val highlights: List<String>,
    /** Resolved assets path for hero image (Step 5: Coil). */
    val imageAssetPath: String? = null
)
