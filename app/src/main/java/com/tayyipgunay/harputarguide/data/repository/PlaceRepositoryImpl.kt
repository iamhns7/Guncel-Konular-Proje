package com.tayyipgunay.harputarguide.data.repository

import com.tayyipgunay.harputarguide.core.common.AppLogger
import com.tayyipgunay.harputarguide.data.asset.SamplePlaces
import com.tayyipgunay.harputarguide.data.content.HarputContentDataSource
import com.tayyipgunay.harputarguide.data.mapper.PlaceContentMapper
import com.tayyipgunay.harputarguide.domain.model.Place
import com.tayyipgunay.harputarguide.domain.model.PlaceDetail
import com.tayyipgunay.harputarguide.domain.repository.PlaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaceRepositoryImpl(
    private val dataSource: HarputContentDataSource,
    private val mapper: PlaceContentMapper = PlaceContentMapper
) : PlaceRepository {

    override suspend fun getPlaces(locale: String): List<Place> = withContext(Dispatchers.IO) {
        runCatching { loadPlacesFromAssets(locale) }
            .onFailure {
                AppLogger.e("getPlaces hata verdi (locale=$locale), örnek listeye düşülüyor.", it)
            }
            .getOrElse { SamplePlaces.list }
    }

    override suspend fun getPlaceDetail(placeId: String, locale: String): PlaceDetail? =
        withContext(Dispatchers.IO) {
            runCatching { loadPlaceDetailFromAssets(placeId, locale) }
                .onFailure {
                    AppLogger.e("getPlaceDetail hata verdi (placeId=$placeId, locale=$locale).", it)
                }
                .getOrNull()
        }

    private fun loadPlacesFromAssets(locale: String): List<Place> {
        val index = dataSource.loadPlacesIndex()
        if (index.isEmpty()) {
            AppLogger.w("Mekân index'i boş/okunamadı; örnek liste kullanılıyor (locale=$locale).")
            return SamplePlaces.list
        }

        val places = index.mapNotNull { item ->
            val meta = dataSource.loadPlaceMeta(locale, item.id)
            when {
                meta != null -> mapper.toPlace(meta, item, isVisited = false)
                else -> {
                    AppLogger.w("Mekân meta bulunamadı (id=${item.id}, locale=$locale); index verisiyle dolduruluyor.")
                    mapper.toPlaceFromIndex(item, isVisited = false)
                }
            }
        }

        return places.takeIf { it.isNotEmpty() } ?: run {
            AppLogger.w("Mekân listesi boş üretildi; örnek liste kullanılıyor (locale=$locale).")
            SamplePlaces.list
        }
    }

    private fun loadPlaceDetailFromAssets(placeId: String, locale: String): PlaceDetail? {
        val meta = dataSource.loadPlaceMeta(locale, placeId) ?: run {
            AppLogger.w("Detay için meta bulunamadı (placeId=$placeId, locale=$locale).")
            return null
        }
        val detailContent = dataSource.loadPlaceDetail(locale, placeId) ?: run {
            AppLogger.w("Detay içeriği bulunamadı (placeId=$placeId, locale=$locale).")
            return null
        }
        return mapper.toPlaceDetail(meta, detailContent, locale)
    }
}
