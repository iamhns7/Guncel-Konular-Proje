package com.tayyipgunay.harputarguide.data.asset

import com.tayyipgunay.harputarguide.domain.model.PlaceDetail

/** AR / navigasyon yedek detayları — yalnızca 5 AR noktası. */
object SamplePlaceDetails {

    private val details = mapOf(
        "harput-kalesi" to PlaceDetail(
            id = "harput-kalesi",
            name = "Harput Kalesi",
            location = "Harput Kalesi, Elazığ",
            description = "Harput Kalesi'nin dış surları, burçları, taş duvar dokusu ve kayalık temel yapısını inceleyen AR deneyimi.",
            period = "Orta Çağ",
            estimatedBuild = "8 – 13. yy",
            category = "Kale",
            highlights = listOf("Sur duvarları", "Burçlar", "Taş duvar dokusu")
        ),
        "harput-kalesi-genis-aci" to PlaceDetail(
            id = "harput-kalesi-genis-aci",
            name = "Harput Kalesi - Geniş Açı",
            location = "Harput Kalesi, Elazığ",
            description = "Harput Kalesi'nin genel kütlesini, sur hatlarını, burçlarını ve kayalık temelini geniş açıdan tanıtan AR deneyimi.",
            period = "Orta Çağ",
            estimatedBuild = "8 – 13. yy",
            category = "Kale",
            highlights = listOf("Ana kale kütlesi", "Sur hattı", "Kayalık temel")
        ),
        "ic-kale-ve-kazi-alani" to PlaceDetail(
            id = "ic-kale-ve-kazi-alani",
            name = "İç Kale ve Kazı Alanı",
            location = "Harput Kalesi, Elazığ",
            description = "İç kale alanındaki kazı zemini, seramik kaplar, çukur alanlar, taş duvarlar ve yapı kalıntılarını tanıtan AR deneyimi.",
            period = "Tarihi Dönem",
            estimatedBuild = "—",
            category = "Kazı Alanı",
            highlights = listOf("Kazı zemini", "Seramik kaplar", "Yapı kalıntıları")
        ),
        "artuklu-sarnici-ve-zindani" to PlaceDetail(
            id = "artuklu-sarnici-ve-zindani",
            name = "Urartu Sarnıcı ve Zindan Geçidi",
            location = "Harput Kalesi, Elazığ",
            description = "Kayaya oyulmuş sarnıç ve zindan geçidi alanını; basamakları, tünel geçidini ve iniş hattını tanıtan AR deneyimi.",
            period = "Urartu / Orta Çağ",
            estimatedBuild = "—",
            category = "Sarnıç ve Zindan",
            highlights = listOf("Tünel geçidi", "Basamaklar", "Kayaya oyulmuş yapı")
        ),
        "ulu-cami" to PlaceDetail(
            id = "ulu-cami",
            name = "Harput Ulu Cami",
            location = "Harput, Elazığ",
            description = "Harput Ulu Cami'nin eğri minaresi, tuğla örgüsü, moloz taş duvarı ve giriş kemerini tanıtan AR deneyimi.",
            period = "Artuklu Dönemi",
            estimatedBuild = "12. yy",
            category = "Cami",
            highlights = listOf("Eğri minare", "Tuğla örgü", "Giriş kemeri")
        )
    )

    fun hasDetail(placeId: String): Boolean = details.containsKey(placeId)

    fun getById(placeId: String): PlaceDetail {
        return details[placeId] ?: PlaceDetail(
            id = placeId,
            name = placeId,
            location = "Harput, Elazığ",
            description = "",
            period = "—",
            estimatedBuild = "—",
            category = "—",
            highlights = emptyList()
        )
    }
}
