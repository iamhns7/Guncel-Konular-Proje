package com.tayyipgunay.harputarguide.data.asset

import com.tayyipgunay.harputarguide.domain.model.Place

object SamplePlaces {
    val list: List<Place> = listOf(
        Place(
            id = "kale_surlari",
            name = "Kale Surları",
            description = "Harput Kalesi'nin etkileyici sur kalıntıları.",
            distance = "1.2 km",
            isVisited = true
        ),
        Place(
            id = "harput_kalesi",
            name = "Harput Kalesi",
            description = "Harput'un simgesi, tarihi kalenin büyüleyici yapısı.",
            distance = "1.3 km",
            isVisited = true
        ),
        Place(
            id = "seyir_noktasi",
            name = "Seyir Noktası",
            description = "Harput'u kuşbakışı görebileceğiniz nokta.",
            distance = "1.6 km",
            isVisited = false
        ),
        Place(
            id = "ulu_cami",
            name = "Ulu Cami",
            description = "Tarihi Ulu Cami kalıntıları.",
            distance = "1.5 km",
            isVisited = false
        ),
        Place(
            id = "kuyu_ve_sarnic",
            name = "Kuyu ve Sarnıç",
            description = "Tarihi su kuyusu ve sarnıçlar.",
            distance = "1.1 km",
            isVisited = true
        ),
        Place(
            id = "kale_giris_kapisi",
            name = "Kale Giriş Kapısı",
            description = "Kalenin ana giriş bölümü ve savunma geçidi.",
            distance = "1.4 km",
            isVisited = false
        ),
        Place(
            id = "ic_kale_ust_alan",
            name = "İç Kale / Üst Alan",
            description = "Kalenin iç bölümünde yer alan tarihi alan.",
            distance = "1.7 km",
            isVisited = false
        ),
        Place(
            id = "sefik_gul_kultur_evi",
            name = "Şefik Gül Kültür Evi",
            description = "Harput kültürünü yansıtan geleneksel yapı.",
            distance = "900 m",
            isVisited = false
        )
    )
}
