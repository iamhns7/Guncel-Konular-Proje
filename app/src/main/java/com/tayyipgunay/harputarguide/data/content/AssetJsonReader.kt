package com.tayyipgunay.harputarguide.data.content

import android.content.Context
import kotlinx.serialization.json.Json

/**
 * Reads UTF-8 text from app assets and parses JSON via kotlinx.serialization.
 */
class AssetJsonReader(
    context: Context,
    val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
) {
    private val assetManager = context.assets

    fun readText(assetPath: String): Result<String> = runCatching {
        val raw = assetManager.open(assetPath).bufferedReader(Charsets.UTF_8).use { it.readText() }
        stripUtf8Bom(raw)
    }

    /** Windows/editör kaynaklı BOM, kotlinx.serialization JSON parse'ını bozar. */
    private fun stripUtf8Bom(text: String): String =
        if (text.isNotEmpty() && text[0] == '\uFEFF') text.substring(1) else text

    inline fun <reified T> decode(assetPath: String): Result<T> =
        readText(assetPath).mapCatching { text ->
            json.decodeFromString<T>(text)
        }

    inline fun <reified T> decodeList(assetPath: String): Result<List<T>> =
        readText(assetPath).mapCatching { text ->
            json.decodeFromString<List<T>>(text)
        }

    fun assetExists(assetPath: String): Boolean = runCatching {
        assetManager.open(assetPath).close()
        true
    }.getOrDefault(false)

    /**
     * Lists file names in an asset directory (non-recursive). Returns empty list if missing.
     */
    fun listAssetFiles(assetDirPath: String): List<String> = runCatching {
        assetManager.list(assetDirPath)?.toList().orEmpty()
    }.getOrDefault(emptyList())
}
