package com.tayyipgunay.harputarguide.data.content

/**
 * Resolves JSON image references (`media/...`, `imageFile`, etc.) to asset paths and URIs.
 */
object ContentAssetPathResolver {

    private const val ANDROID_ASSET_URI_PREFIX = "file:///android_asset/"

    /**
     * Converts a JSON [imageReference] to a path relative to [android.content.res.AssetManager].
     *
     * Supported inputs:
     * - `media/Harput-kalesi.jpg`
     * - `Harput-kalesi.jpg`
     * - `content/harput-content-export/images/Harput-kalesi.jpg`
     * - `images/Harput-kalesi.jpg`
     */
    fun resolveImageAssetPath(imageReference: String?): String? {
        if (imageReference.isNullOrBlank()) return null

        val trimmed = imageReference.trim().trimStart('/')
        if (trimmed.startsWith(ContentAssetPaths.IMAGES_DIR)) {
            return trimmed
        }
        if (trimmed.startsWith("images/")) {
            return "${ContentAssetPaths.CONTENT_ROOT}/$trimmed"
        }

        val withoutMediaPrefix = when {
            trimmed.startsWith("media/") -> trimmed.removePrefix("media/")
            trimmed.startsWith("Media/") -> trimmed.removePrefix("Media/")
            else -> trimmed
        }
        val fileName = withoutMediaPrefix.substringAfterLast('/').trim()
        if (fileName.isEmpty()) return null
        return "${ContentAssetPaths.IMAGES_DIR}/$fileName"
    }

    /** `file:///android_asset/{assetPath}` for Coil and other image loaders. */
    fun toAndroidAssetUri(assetPath: String): String =
        ANDROID_ASSET_URI_PREFIX + assetPath.trimStart('/')

    /**
     * Coil model from [imageAssetPath] (resolved path, raw reference, or existing asset URI).
     * Returns null when no usable path is available.
     */
    fun toImageModel(imageAssetPath: String?): String? {
        if (imageAssetPath.isNullOrBlank()) return null
        val trimmed = imageAssetPath.trim()
        if (trimmed.startsWith(ANDROID_ASSET_URI_PREFIX)) return trimmed

        val resolvedPath = resolveImageAssetPath(trimmed) ?: return null
        return toAndroidAssetUri(resolvedPath)
    }
}
