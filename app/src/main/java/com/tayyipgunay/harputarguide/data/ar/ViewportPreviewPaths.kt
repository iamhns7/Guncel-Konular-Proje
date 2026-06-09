package com.tayyipgunay.harputarguide.data.ar

object ViewportPreviewPaths {
    private const val ROOT = "${HotspotContentPaths.ROOT}/viewport_previews"

    fun forHotspot(placeId: String, hotspotId: String): String =
        "$ROOT/$placeId-$hotspotId.png"
}
