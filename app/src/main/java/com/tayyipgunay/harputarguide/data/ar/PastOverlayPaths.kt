package com.tayyipgunay.harputarguide.data.ar

object PastOverlayPaths {
    private const val ROOT = "ar/past_overlays"

    val supportedPlaceIds = setOf(
        "harput-kalesi",
        "ulu-cami"
    )

    fun forPlace(placeId: String): String? = when (placeId) {
        "harput-kalesi" -> "$ROOT/harput-kalesi.jpg"
        "ulu-cami" -> "$ROOT/ulu-cami.jpg"
        else -> null
    }
}
