package com.tayyipgunay.harputarguide.data.content

/**
 * Asset paths under [android.content.res.AssetManager] for the Harput content export package.
 */
object ContentAssetPaths {

    const val CONTENT_ROOT = "content/harput-content-export"

    const val MANIFEST = "$CONTENT_ROOT/manifest.json"

    const val PLACES_INDEX = "$CONTENT_ROOT/places-index.json"

    const val IMAGES_DIR = "$CONTENT_ROOT/images"

    fun localeRoot(locale: String): String = "$CONTENT_ROOT/$locale"

    fun placesMetaDir(locale: String): String = "$CONTENT_ROOT/$locale/places/meta"

    fun placesDetailDir(locale: String): String = "$CONTENT_ROOT/$locale/places/detail"

    fun placeMetaPath(locale: String, placeId: String): String =
        "$CONTENT_ROOT/$locale/places/meta/$placeId.json"

    fun placeDetailPath(locale: String, placeId: String): String =
        "$CONTENT_ROOT/$locale/places/detail/$placeId.json"

    fun timelinesMetaDir(locale: String): String = "$CONTENT_ROOT/$locale/timelines/meta"

    fun timelinesStoryDir(locale: String): String = "$CONTENT_ROOT/$locale/timelines/story"

    fun timelineMetaPath(locale: String, timelineId: String): String =
        "$CONTENT_ROOT/$locale/timelines/meta/$timelineId.json"

    fun timelineStoryPath(locale: String, timelineId: String): String =
        "$CONTENT_ROOT/$locale/timelines/story/$timelineId.json"
}
