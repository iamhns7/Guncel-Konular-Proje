package com.tayyipgunay.harputarguide.data.mapper

import com.tayyipgunay.harputarguide.core.locale.AppLanguage
import com.tayyipgunay.harputarguide.data.content.ContentAssetPathResolver
import com.tayyipgunay.harputarguide.data.dto.PlaceDetailContentDto
import com.tayyipgunay.harputarguide.data.dto.PlaceIndexItemDto
import com.tayyipgunay.harputarguide.data.dto.PlaceMetaDto
import com.tayyipgunay.harputarguide.domain.model.Place
import com.tayyipgunay.harputarguide.domain.model.PlaceDetail

object PlaceContentMapper {

    private const val DEFAULT_LOCATION_TR = "Harput, Elazığ"
    private const val DEFAULT_LOCATION_EN = "Harput, Elazığ, Türkiye"
    private const val MISSING_VALUE = "—"
    private const val MISSING_DISTANCE = "—"

    fun toPlace(
        meta: PlaceMetaDto,
        indexItem: PlaceIndexItemDto? = null,
        isVisited: Boolean = false
    ): Place {
        val imageReference = meta.imageAsset ?: indexItem?.imageFile
        return Place(
            id = meta.id,
            name = formatDisplayName(meta.name),
            description = meta.shortDescription?.takeIf { it.isNotBlank() }
                ?: indexItem?.yearOrEra.orEmpty(),
            distance = MISSING_DISTANCE,
            isVisited = isVisited,
            imageAssetPath = ContentAssetPathResolver.resolveImageAssetPath(imageReference)
        )
    }

    fun toPlaceFromIndex(
        indexItem: PlaceIndexItemDto,
        isVisited: Boolean = false
    ): Place {
        return Place(
            id = indexItem.id,
            name = formatDisplayName(indexItem.name),
            description = indexItem.yearOrEra?.takeIf { it.isNotBlank() }.orEmpty(),
            distance = MISSING_DISTANCE,
            isVisited = isVisited,
            imageAssetPath = ContentAssetPathResolver.resolveImageAssetPath(indexItem.imageFile)
        )
    }

    fun toPlaceDetail(
        meta: PlaceMetaDto,
        detail: PlaceDetailContentDto,
        locale: String = AppLanguage.DEFAULT
    ): PlaceDetail {
        return PlaceDetail(
            id = detail.id,
            name = formatDisplayName(meta.name),
            location = defaultLocation(locale),
            description = detail.about?.takeIf { it.isNotBlank() }.orEmpty(),
            period = meta.yearOrEra?.takeIf { it.isNotBlank() } ?: MISSING_VALUE,
            estimatedBuild = MISSING_VALUE,
            category = formatCategory(meta.category, locale),
            highlights = emptyList(),
            imageAssetPath = ContentAssetPathResolver.resolveImageAssetPath(meta.imageAsset)
        )
    }

    private fun formatDisplayName(rawName: String): String {
        return rawName.trim()
            .lowercase()
            .split(' ', '-', '_')
            .filter { it.isNotBlank() }
            .joinToString(" ") { word ->
                word.replaceFirstChar { char ->
                    if (char.isLowerCase()) char.titlecase() else char.toString()
                }
            }
    }

    private fun defaultLocation(locale: String): String =
        if (AppLanguage.normalize(locale) == AppLanguage.EN) DEFAULT_LOCATION_EN else DEFAULT_LOCATION_TR

    private fun formatCategory(rawCategory: String?, locale: String): String {
        val isEnglish = AppLanguage.normalize(locale) == AppLanguage.EN
        return when (rawCategory?.lowercase()) {
            "historical" -> if (isEnglish) "Historical site" else "Tarihi yapı"
            "castle" -> if (isEnglish) "Castle" else "Kale"
            "mosque" -> if (isEnglish) "Mosque" else "Cami"
            "ruins" -> if (isEnglish) "Ruins" else "Kalıntı"
            null, "" -> MISSING_VALUE
            else -> rawCategory.replaceFirstChar { it.uppercase() }
        }
    }
}
