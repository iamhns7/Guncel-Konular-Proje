package com.tayyipgunay.harputarguide.data.asset

import com.tayyipgunay.harputarguide.domain.model.PlaceDetail

object SamplePlaceDetails {

    private val details = mapOf(
        "kale_surlari" to PlaceDetail(
            id = "kale_surlari",
            name = "Kale Surları",
            location = "Harput Kalesi, Elazığ",
            description = "Harput Kalesi'nin koruyucu surları, bölgeyi dış tehditlere karşı savunmak amacıyla inşa edilmiştir. Kalın taş duvarları ve stratejik konumuyla Orta Çağ savunma mimarisinin önemli örneklerinden biridir.",
            period = "Orta Çağ",
            estimatedBuild = "10 – 13. yy",
            category = "Savunma Yapısı",
            highlights = listOf(
                "Kalın taş duvarlar",
                "Stratejik konum",
                "Panoramik manzara"
            )
        ),
        "harput_kalesi" to PlaceDetail(
            id = "harput_kalesi",
            name = "Harput Kalesi",
            location = "Harput Kalesi, Elazığ",
            description = "Harput'un simgesi olan tarihi kale, yüzyıllar boyunca bölgenin savunma ve yönetim merkezi olarak kullanılmıştır.",
            period = "Orta Çağ",
            estimatedBuild = "8 – 13. yy",
            category = "Kale",
            highlights = listOf(
                "Tarihi surlar",
                "Geniş manzara",
                "Kültürel miras"
            )
        ),
        "seyir_noktasi" to PlaceDetail(
            id = "seyir_noktasi",
            name = "Seyir Noktası",
            location = "Harput, Elazığ",
            description = "Harput'u kuşbakışı görebileceğiniz seyir noktası, ziyaretçilere geniş bir panorama sunar.",
            period = "Modern Dönem",
            estimatedBuild = "20. yy",
            category = "Seyir Noktası",
            highlights = listOf(
                "Panoramik görüş",
                "Fotoğraf noktası",
                "Kolay erişim"
            )
        ),
        "ulu_cami" to PlaceDetail(
            id = "ulu_cami",
            name = "Ulu Cami",
            location = "Harput, Elazığ",
            description = "Tarihi Ulu Cami kalıntıları, Harput'un dini ve kültürel geçmişine ışık tutan önemli bir yapıdır.",
            period = "Selçuklu Dönemi",
            estimatedBuild = "12. yy",
            category = "Dini Yapı",
            highlights = listOf(
                "Tarihi mimari",
                "Taş işçiliği",
                "Kültürel önem"
            )
        ),
        "kuyu_ve_sarnic" to PlaceDetail(
            id = "kuyu_ve_sarnic",
            name = "Kuyu ve Sarnıç",
            location = "Harput Kalesi, Elazığ",
            description = "Kalenin su ihtiyacını karşılayan tarihi kuyu ve sarnıç yapıları, savunma yaşamının önemli parçalarıdır.",
            period = "Orta Çağ",
            estimatedBuild = "10 – 13. yy",
            category = "Altyapı",
            highlights = listOf(
                "Tarihi su yapıları",
                "Savunma desteği",
                "Arkeolojik değer"
            )
        ),
        "kale_giris_kapisi" to PlaceDetail(
            id = "kale_giris_kapisi",
            name = "Kale Giriş Kapısı",
            location = "Harput Kalesi, Elazığ",
            description = "Kalenin ana giriş bölümü ve savunma geçidi, ziyaretçileri tarihi kale alanına karşılar.",
            period = "Orta Çağ",
            estimatedBuild = "11 – 13. yy",
            category = "Savunma Yapısı",
            highlights = listOf(
                "Ana giriş",
                "Taş kemer",
                "Tarihi geçit"
            )
        ),
        "ic_kale_ust_alan" to PlaceDetail(
            id = "ic_kale_ust_alan",
            name = "İç Kale / Üst Alan",
            location = "Harput Kalesi, Elazığ",
            description = "Kalenin iç bölümünde yer alan üst alan, tarihi yapı kalıntıları ve geniş bir görüş alanı sunar.",
            period = "Orta Çağ",
            estimatedBuild = "10 – 13. yy",
            category = "Kale Alanı",
            highlights = listOf(
                "İç kale alanı",
                "Tarihi kalıntılar",
                "Yüksek konum"
            )
        ),
        "sefik_gul_kultur_evi" to PlaceDetail(
            id = "sefik_gul_kultur_evi",
            name = "Şefik Gül Kültür Evi",
            location = "Harput, Elazığ",
            description = "Harput kültürünü yansıtan geleneksel yapı, bölgenin sosyal ve kültürel yaşamına dair ipuçları sunar.",
            period = "Erken Cumhuriyet",
            estimatedBuild = "20. yy",
            category = "Kültür Evi",
            highlights = listOf(
                "Geleneksel mimari",
                "Kültürel etkinlikler",
                "Yerel miras"
            )
        )
    )

    fun getById(placeId: String): PlaceDetail {
        return details[placeId] ?: PlaceDetail(
            id = placeId,
            name = placeId.replace('_', ' ').replaceFirstChar { it.uppercase() },
            location = "Harput, Elazığ",
            description = "Bu tarihi nokta hakkında detaylı bilgiler yakında eklenecektir.",
            period = "Tarihi Dönem",
            estimatedBuild = "—",
            category = "Tarihi Nokta",
            highlights = listOf(
                "Tarihi değer",
                "Kültürel miras",
                "Ziyaret noktası"
            )
        )
    }
}
