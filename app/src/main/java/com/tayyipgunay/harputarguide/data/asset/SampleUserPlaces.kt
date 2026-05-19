package com.tayyipgunay.harputarguide.data.asset

import com.tayyipgunay.harputarguide.domain.model.Place

object SampleUserPlaces {

    private val favoriteIds = listOf(
        "kale_surlari",
        "harput_kalesi",
        "ulu_cami",
        "seyir_noktasi"
    )

    fun getFavorites(): List<Place> =
        SamplePlaces.list.filter { it.id in favoriteIds }

    fun getVisited(): List<Place> =
        SamplePlaces.list.filter { it.isVisited }
}
