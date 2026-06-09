package com.tayyipgunay.harputarguide.feature.modelviewer

import com.tayyipgunay.harputarguide.core.design.component.analysis.AnalysisCallout

/**
 * Referans mockup'lar: docs/design/3d-detail-screens/
 */
data class ModelCalloutLabel(
    val title: String,
    val anchorY: Float = 0.5f,
    val anchorX: Float = 0.54f
) {
    fun toAnalysisCallout(): AnalysisCallout = AnalysisCallout(
        label = title,
        anchorYFraction = anchorY,
        anchorXFraction = anchorX
    )
}

object ModelCalloutLabels {
    fun forHotspot(
        placeId: String,
        hotspotId: String,
        modelFileName: String
    ): List<ModelCalloutLabel> {
        val key = "$placeId/$hotspotId"
        return hotspotCallouts[key] ?: forModelFile(modelFileName)
    }

    private val hotspotCallouts: Map<String, List<ModelCalloutLabel>> = mapOf(
        "harput-kalesi/ana_burc" to labels3(
            "Burç Gövdesi" to 0.26f,
            "Üst Savunma Hattı" to 0.48f,
            "Gözetleme Alanı" to 0.70f
        ),
        "harput-kalesi/tas_duvar_dokusu" to labels3(
            "Taş Yüzey" to 0.24f,
            "Harç / Derz" to 0.48f,
            "İç Dolgu" to 0.68f
        ),
        "harput-kalesi/sag_burc" to labels3(
            "Burç Gövdesi" to 0.28f,
            "Cephe Kontrolü" to 0.50f,
            "Savunma Hattı" to 0.72f
        ),
        "harput-kalesi-genis-aci/orta_burc" to labels3(
            "Burç Gövdesi" to 0.26f,
            "Üst Savunma Hattı" to 0.48f,
            "Ara Kontrol Noktası" to 0.70f
        ),
        "harput-kalesi-genis-aci/sag_burc" to labels3(
            "Burç Gövdesi" to 0.28f,
            "Cephe Kontrolü" to 0.50f,
            "Gözetleme Alanı" to 0.72f
        ),
        "ic-kale-ve-kazi-alani/seramik_kap_kup" to labels4(
            "Ağız Bölümü" to 0.20f,
            "Gövde" to 0.38f,
            "İç Hacim" to 0.56f,
            "Taban" to 0.74f
        ),
        "ic-kale-ve-kazi-alani/tas_duvar" to labels4(
            "Taş Yüzey" to 0.22f,
            "Harç / Derz" to 0.40f,
            "Taş Örgü" to 0.58f,
            "İç Dolgu" to 0.76f
        ),
        "artuklu-sarnici-ve-zindani/kaya_oyma_yuzey" to labels3(
            "Kaya Oyma Yüzey" to 0.28f,
            "Doğal Kaya" to 0.50f,
            "Yapı Tekniği" to 0.72f
        ),
        "artuklu-sarnici-ve-zindani/basamaklar" to labels3(
            "Basamak Hattı" to 0.30f,
            "İniş Yönü" to 0.52f,
            "Kaya Yüzeyi" to 0.74f
        ),
        "artuklu-sarnici-ve-zindani/tunel_gecidi" to labels3(
            "Tünel Geçidi" to 0.28f,
            "Geçiş Hattı" to 0.50f,
            "Zemin / İniş" to 0.72f
        ),
        "ulu-cami/egri_minare" to labels3(
            "Minare Gövdesi" to 0.26f,
            "Eğim Yönü" to 0.50f,
            "Üst Bölüm" to 0.72f
        ),
        "ulu-cami/tugla_orgu" to labels3(
            "Tuğla Örgü" to 0.26f,
            "Yüzey Ritmi" to 0.50f,
            "Gövde Dokusu" to 0.72f
        ),
        "ulu-cami/moloz_tas_duvar" to labels3(
            "Moloz Taş" to 0.26f,
            "Harç / Derz" to 0.50f,
            "Duvar Dokusu" to 0.72f
        )
    )

    fun forModelFile(modelFileName: String): List<ModelCalloutLabel> {
        return when (modelFileName.lowercase()) {
            "wall-section.glb" -> labels3(
                "Taş Yüzey" to 0.24f,
                "Harç / Derz" to 0.48f,
                "İç Dolgu" to 0.68f
            )
            "tower-bastion.glb" -> labels3(
                "Burç Gövdesi" to 0.26f,
                "Üst Savunma Hattı" to 0.48f,
                "Gözetleme Alanı" to 0.70f
            )
            "ceramic-jar.glb" -> labels4(
                "Ağız Bölümü" to 0.20f,
                "Gövde" to 0.38f,
                "Taban" to 0.56f,
                "İç Hacim" to 0.74f
            )
            "rock-cut-tunnel.glb" -> labels4(
                "Kaya Oyma Yüzey" to 0.22f,
                "Basamak Hattı" to 0.40f,
                "Tünel Geçidi" to 0.58f,
                "Zemin / İniş Yönü" to 0.76f
            )
            "leaning-minaret.glb" -> labels4(
                "Minare Gövdesi" to 0.24f,
                "Tuğla Örgü" to 0.42f,
                "Eğim Yönü" to 0.60f,
                "Üst Bölüm" to 0.78f
            )
            else -> emptyList()
        }
    }

    private fun labels3(vararg entries: Pair<String, Float>): List<ModelCalloutLabel> =
        entries.map { (title, anchorY) -> ModelCalloutLabel(title, anchorY) }

    private fun labels4(vararg entries: Pair<String, Float>): List<ModelCalloutLabel> =
        entries.map { (title, anchorY) -> ModelCalloutLabel(title, anchorY) }
}
