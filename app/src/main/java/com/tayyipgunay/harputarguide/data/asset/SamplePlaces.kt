package com.tayyipgunay.harputarguide.data.asset

import com.tayyipgunay.harputarguide.domain.model.Place

/** Asset JSON okunamadığında kullanılan yedek liste — yalnızca 5 AR noktası. */
object SamplePlaces {
    val list: List<Place> = listOf(
        Place(
            id = "harput-kalesi",
            name = "Harput Kalesi",
            description = "Harput Kalesi'nin dış surları, burçları ve taş duvar dokusu.",
            distance = "1.3 km",
            isVisited = false
        ),
        Place(
            id = "harput-kalesi-genis-aci",
            name = "Harput Kalesi - Geniş Açı",
            description = "Kalenin genel kütlesi, sur hatları ve burçları geniş açıdan.",
            distance = "1.3 km",
            isVisited = false
        ),
        Place(
            id = "ic-kale-ve-kazi-alani",
            name = "İç Kale ve Kazı Alanı",
            description = "Kazı zemini, seramik kaplar ve yapı kalıntıları.",
            distance = "1.2 km",
            isVisited = false
        ),
        Place(
            id = "artuklu-sarnici-ve-zindani",
            name = "Urartu Sarnıcı ve Zindan Geçidi",
            description = "Kayaya oyulmuş sarnıç ve zindan geçidi.",
            distance = "1.1 km",
            isVisited = false
        ),
        Place(
            id = "ulu-cami",
            name = "Harput Ulu Cami",
            description = "Eğri minare, tuğla örgü ve moloz taş duvarlar.",
            distance = "1.5 km",
            isVisited = false
        )
    )
}
