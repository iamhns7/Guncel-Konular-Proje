package com.tayyipgunay.harputarguide.domain.model

data class Place(
    val id: String,
    val name: String,
    val description: String,
    val distance: String,
    val isVisited: Boolean,
    /** Resolved assets path for hero/card image (Step 5: Coil). */
    val imageAssetPath: String? = null
)
