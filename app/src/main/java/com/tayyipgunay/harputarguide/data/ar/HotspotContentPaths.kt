package com.tayyipgunay.harputarguide.data.ar

object HotspotContentPaths {
    const val ROOT = "ar/hotspot_contents"
    const val MANIFEST = "$ROOT/hotspot_contents.json"

    fun placePath(placeId: String): String = "$ROOT/places/$placeId.json"

    fun resolveAssetPath(relativePath: String): String = when {
        relativePath.startsWith("$ROOT/") -> relativePath
        else -> "$ROOT/$relativePath"
    }
}
