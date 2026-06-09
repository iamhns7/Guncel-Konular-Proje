package com.tayyipgunay.harputarguide.data.map

import com.tayyipgunay.harputarguide.domain.model.MapPoint

object HarputMapPoints {
    val harputMapPoints = listOf(
        MapPoint(
            placeId = "harput-kalesi",
            title = "Harput Kalesi",
            latitude = 38.703448,
            longitude = 39.257222
        ),
        MapPoint(
            placeId = "harput-kalesi-genis-aci",
            title = "Harput Kalesi Geniş Açı",
            latitude = 38.703781,
            longitude = 39.257156
        ),
        MapPoint(
            placeId = "ic-kale-ve-kazi-alani",
            title = "İç Kale ve Kazı Alanı",
            latitude = 38.704032,
            longitude = 39.256983
        ),
        MapPoint(
            placeId = "artuklu-sarnici-ve-zindani",
            title = "Urartu Sarnıcı ve Zindan Geçidi",
            latitude = 38.704200,
            longitude = 39.256800
        ),
        MapPoint(
            placeId = "ulu-cami",
            title = "Harput Ulu Cami",
            latitude = 38.706111,
            longitude = 39.255139
        )
    )

    fun findByPlaceId(placeId: String): MapPoint? =
        harputMapPoints.find { it.placeId == placeId }
}
