package com.tayyipgunay.harputarguide.domain.repository

import com.tayyipgunay.harputarguide.domain.model.Place
import com.tayyipgunay.harputarguide.domain.model.PlaceDetail

interface PlaceRepository {
    suspend fun getPlaces(locale: String): List<Place>
    suspend fun getPlaceDetail(placeId: String, locale: String): PlaceDetail?
}
