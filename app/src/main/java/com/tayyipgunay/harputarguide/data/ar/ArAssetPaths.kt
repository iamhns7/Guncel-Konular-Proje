package com.tayyipgunay.harputarguide.data.ar

object ArAssetPaths {
    const val ROOT = "ar"
    const val MANIFEST = "$ROOT/manifest.json"

    fun placeContentPath(placeId: String): String = "$ROOT/places/$placeId.json"
}
