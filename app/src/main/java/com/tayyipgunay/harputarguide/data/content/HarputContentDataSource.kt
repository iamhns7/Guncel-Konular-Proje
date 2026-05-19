package com.tayyipgunay.harputarguide.data.content

import android.content.Context
import com.tayyipgunay.harputarguide.data.dto.ContentManifestDto
import com.tayyipgunay.harputarguide.data.dto.PlaceDetailContentDto
import com.tayyipgunay.harputarguide.data.dto.PlaceIndexItemDto
import com.tayyipgunay.harputarguide.data.dto.PlaceMetaDto
import com.tayyipgunay.harputarguide.data.dto.TimelineMetaDto
import com.tayyipgunay.harputarguide.data.dto.TimelineStoryDto

/**
 * Reads Harput content JSON from assets with in-memory caching and locale fallback.
 *
 * Not wired to UI yet — screens continue using [com.tayyipgunay.harputarguide.data.asset] samples.
 */
class HarputContentDataSource(context: Context) {

    private val reader = AssetJsonReader(context)

    private var manifestCache: ContentManifestDto? = null
    private var placesIndexCache: List<PlaceIndexItemDto>? = null
    private val placeMetaCache = mutableMapOf<String, PlaceMetaDto>()
    private val placeDetailCache = mutableMapOf<String, PlaceDetailContentDto>()
    private val timelineMetaCache = mutableMapOf<String, TimelineMetaDto>()
    private val timelineStoryCache = mutableMapOf<String, TimelineStoryDto>()
    private val timelineIdsByPlaceCache = mutableMapOf<String, List<String>>()

    // region Debug / manual inspection (non-suspend)

    fun loadManifest(): ContentManifestDto? {
        manifestCache?.let { return it }
        val manifest = reader.decode<ContentManifestDto>(ContentAssetPaths.MANIFEST).getOrNull()
        manifestCache = manifest
        return manifest
    }

    fun loadPlacesIndex(): List<PlaceIndexItemDto> {
        placesIndexCache?.let { return it }
        val index = reader.decodeList<PlaceIndexItemDto>(ContentAssetPaths.PLACES_INDEX)
            .getOrNull()
            .orEmpty()
        placesIndexCache = index
        return index
    }

    fun loadPlaceMeta(locale: String, placeId: String): PlaceMetaDto? =
        loadPlaceMetaInternal(locale, placeId)

    fun loadPlaceDetail(locale: String, placeId: String): PlaceDetailContentDto? =
        loadPlaceDetailInternal(locale, placeId)

    // endregion

    // region Timeline (prepared for Step 3+)

    fun loadTimelineMeta(locale: String, timelineId: String): TimelineMetaDto? {
        val cacheKey = timelineMetaCacheKey(locale, timelineId)
        timelineMetaCache[cacheKey]?.let { return it }
        for (resolvedLocale in localeFallbackChain(locale)) {
            val path = ContentAssetPaths.timelineMetaPath(resolvedLocale, timelineId)
            val dto = reader.decode<TimelineMetaDto>(path).getOrNull() ?: continue
            timelineMetaCache[cacheKey] = dto
            return dto
        }
        return null
    }

    fun loadTimelineStory(locale: String, timelineId: String): TimelineStoryDto? {
        val cacheKey = timelineStoryCacheKey(locale, timelineId)
        timelineStoryCache[cacheKey]?.let { return it }
        for (resolvedLocale in localeFallbackChain(locale)) {
            val path = ContentAssetPaths.timelineStoryPath(resolvedLocale, timelineId)
            val dto = reader.decode<TimelineStoryDto>(path).getOrNull() ?: continue
            timelineStoryCache[cacheKey] = dto
            return dto
        }
        return null
    }

    /**
     * Timeline meta file names (without .json) for a place, using [TimelineMetaDto.placeId].
     */
    fun listTimelineIdsForPlace(locale: String, placeId: String): List<String> {
        val cacheKey = "${locale.lowercase()}:$placeId"
        timelineIdsByPlaceCache[cacheKey]?.let { return it }
        val ids = mutableListOf<String>()
        for (resolvedLocale in localeFallbackChain(locale)) {
            val dir = ContentAssetPaths.timelinesMetaDir(resolvedLocale)
            val files = reader.listAssetFiles(dir).filter { it.endsWith(".json", ignoreCase = true) }
            for (fileName in files) {
                val timelineId = fileName.removeSuffix(".json")
                val meta = loadTimelineMeta(resolvedLocale, timelineId) ?: continue
                if (meta.placeId == placeId) {
                    ids.add(timelineId)
                }
            }
            if (ids.isNotEmpty()) break
        }
        val distinct = ids.distinct()
        timelineIdsByPlaceCache[cacheKey] = distinct
        return distinct
    }

    // endregion

    fun resolveLocale(requestedLocale: String?): String {
        val requested = requestedLocale?.lowercase()?.takeIf { it.isNotBlank() } ?: DEFAULT_LOCALE
        val supported = supportedLocales()
        if (requested in supported) return requested
        if (DEFAULT_LOCALE in supported) return DEFAULT_LOCALE
        if (FALLBACK_LOCALE in supported) return FALLBACK_LOCALE
        return supported.firstOrNull() ?: DEFAULT_LOCALE
    }

    fun localeFallbackChain(requestedLocale: String?): List<String> {
        val supported = supportedLocales()
        val chain = mutableListOf<String>()
        val requested = requestedLocale?.lowercase()?.takeIf { it.isNotBlank() }
        if (requested != null && requested in supported) {
            chain.add(requested)
        }
        if (DEFAULT_LOCALE !in chain && DEFAULT_LOCALE in supported) {
            chain.add(DEFAULT_LOCALE)
        }
        if (FALLBACK_LOCALE !in chain && FALLBACK_LOCALE in supported) {
            chain.add(FALLBACK_LOCALE)
        }
        return chain
    }

    private fun loadPlaceMetaInternal(requestedLocale: String, placeId: String): PlaceMetaDto? {
        val cacheKey = placeMetaCacheKey(requestedLocale, placeId)
        placeMetaCache[cacheKey]?.let { return it }
        for (locale in localeFallbackChain(requestedLocale)) {
            val path = ContentAssetPaths.placeMetaPath(locale, placeId)
            val dto = reader.decode<PlaceMetaDto>(path).getOrNull() ?: continue
            placeMetaCache[cacheKey] = dto
            return dto
        }
        return null
    }

    private fun loadPlaceDetailInternal(
        requestedLocale: String,
        placeId: String
    ): PlaceDetailContentDto? {
        val cacheKey = placeDetailCacheKey(requestedLocale, placeId)
        placeDetailCache[cacheKey]?.let { return it }
        for (locale in localeFallbackChain(requestedLocale)) {
            val path = ContentAssetPaths.placeDetailPath(locale, placeId)
            val dto = reader.decode<PlaceDetailContentDto>(path).getOrNull() ?: continue
            placeDetailCache[cacheKey] = dto
            return dto
        }
        return null
    }

    private fun supportedLocales(): Set<String> {
        val fromManifest = loadManifest()
            ?.supportedLocales
            ?.map { it.lowercase() }
            ?.toSet()
        if (!fromManifest.isNullOrEmpty()) return fromManifest
        return setOf(DEFAULT_LOCALE, FALLBACK_LOCALE)
    }

    private fun placeMetaCacheKey(locale: String, placeId: String) =
        "${locale.lowercase()}:$placeId:meta"

    private fun placeDetailCacheKey(locale: String, placeId: String) =
        "${locale.lowercase()}:$placeId:detail"

    private fun timelineMetaCacheKey(locale: String, timelineId: String) =
        "${locale.lowercase()}:$timelineId:meta"

    private fun timelineStoryCacheKey(locale: String, timelineId: String) =
        "${locale.lowercase()}:$timelineId:story"

    companion object {
        const val DEFAULT_LOCALE = "tr"
        const val FALLBACK_LOCALE = "en"
    }
}
