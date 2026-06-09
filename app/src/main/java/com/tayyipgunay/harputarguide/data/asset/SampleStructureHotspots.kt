package com.tayyipgunay.harputarguide.data.asset

data class StructureHotspot(
    val id: String,
    val name: String,
    val shortDescription: String,
    val x: Float,
    val y: Float,
    val subtitle: String? = null
)

data class HotspotProperty(
    val label: String,
    val value: String
)

data class ConstructionStep(
    val order: Int,
    val title: String,
    val description: String
)

data class MaterialInfoRow(
    val label: String,
    val value: String
)

data class MaterialLayerLabel(
    val title: String,
    val yRatio: Float
)

data class HotspotDetailData(
    val hotspotId: String,
    val name: String,
    val longDescription: String,
    val properties: List<HotspotProperty>,
    val constructionSteps: List<ConstructionStep>,
    val materialRows: List<MaterialInfoRow>,
    val materialLayers: List<MaterialLayerLabel>
)

object SampleStructureHotspots {

    private val flagshipHotspots = listOf(
        StructureHotspot(
            id = "burc",
            name = "Burç",
            shortDescription = "Kalenin savunma gücünü artıran kule bölümü.",
            x = 0.72f,
            y = 0.23f
        ),
        StructureHotspot(
            id = "savunma_acikligi",
            name = "Savunma Açıklığı",
            shortDescription = "Ok, taş ve gözetleme için kullanılan açıklık.",
            x = 0.20f,
            y = 0.40f,
            subtitle = "(Mazgal)"
        ),
        StructureHotspot(
            id = "kesme_tas",
            name = "Kesme Taş",
            shortDescription = "Duvar örgüsünde kullanılan ana taş malzeme.",
            x = 0.60f,
            y = 0.46f,
            subtitle = "(Duvar Örgüsü)"
        ),
        StructureHotspot(
            id = "temel_tasi",
            name = "Temel Taşı",
            shortDescription = "Yapının temelini oluşturan taşıyıcı taşlar.",
            x = 0.25f,
            y = 0.75f
        ),
        StructureHotspot(
            id = "harc_derz",
            name = "Harç (Derz)",
            shortDescription = "Taşları bağlayan kireç harcı derzleri.",
            x = 0.70f,
            y = 0.72f
        )
    )

    private val kesmeTasDetail = HotspotDetailData(
        hotspotId = "kesme_tas",
        name = "Kesme Taş",
        longDescription = "Harput Kalesi'nde kullanılan kesme taşlar, yörede çıkarılan kalker ve andezit taşlarından elde edilmiştir. Taşlar belirli boyutlarda kesilerek, harç ile birbirine kenetlenmiştir.",
        properties = listOf(
            HotspotProperty("Taş Türü", "Kalker, Andezit"),
            HotspotProperty("Dayanıklılık", "Yüksek"),
            HotspotProperty("Dönem", "Orta Çağ"),
            HotspotProperty("Renk", "Açık gri - bej")
        ),
        constructionSteps = listOf(
            ConstructionStep(1, "Taş Ocağından Çıkarma", "Taşlar, yakındaki ocaklardan elde edilirdi."),
            ConstructionStep(2, "Kesme ve Şekillendirme", "Ustalar taşları, özel aletlerle kesip şekillendirirdi."),
            ConstructionStep(3, "Taşıma", "Taşlar, hayvan gücü ve kızaklar ile şantiye sahasına taşınırdı."),
            ConstructionStep(4, "Yerleştirme", "Taşlar belirli sıra düzeniyle yerleştirilir, aralarına harç doldurulurdu."),
            ConstructionStep(5, "Kuruma ve Dayanıklılık", "Harç zamanla sertleşir ve yapı dayanıklılık kazanırdı.")
        ),
        materialRows = listOf(
            MaterialInfoRow("Kesme Taş", "Kalker, Andezit"),
            MaterialInfoRow("Harç Türü", "Kireç Harcı"),
            MaterialInfoRow("Dolgu Malzemesi", "Moloz Taş"),
            MaterialInfoRow("Tahmini Yapım Tarihi", "12. - 13. Yüzyıl")
        ),
        materialLayers = listOf(
            MaterialLayerLabel("Kesme Taş (Yüzey Taşı)", 0.28f),
            MaterialLayerLabel("Kireç Harcı (Derz)", 0.48f),
            MaterialLayerLabel("Dolgu Taşı (İç Dolgu)", 0.68f)
        )
    )

    private val defaultDetail = HotspotDetailData(
        hotspotId = "default",
        name = "Yapı Elemanı",
        longDescription = "Bu yapı elemanı hakkında detaylı bilgiler yakında eklenecektir.",
        properties = listOf(
            HotspotProperty("Tür", "Tarihi yapı"),
            HotspotProperty("Dönem", "Orta Çağ")
        ),
        constructionSteps = listOf(
            ConstructionStep(1, "Hazırlık", "Yapı alanı hazırlanır."),
            ConstructionStep(2, "İnşa", "Taşlar yerleştirilir."),
            ConstructionStep(3, "Tamamlama", "Yapı kullanıma hazır hale getirilir.")
        ),
        materialRows = listOf(
            MaterialInfoRow("Malzeme", "Taş"),
            MaterialInfoRow("Bağlayıcı", "Kireç Harcı")
        ),
        materialLayers = listOf(
            MaterialLayerLabel("Yüzey Katmanı", 0.35f),
            MaterialLayerLabel("İç Dolgu", 0.65f)
        )
    )

    /**
     * Demo aşaması: [FLAGSHIP_PLACE_ID] için tanımlı örnek hotspotlar döner.
     * Diğer mekânlar için de aynı örnek set gösterilir (gerçek entegrasyonda placeId bazlı olacak).
     */
    fun getHotspots(placeId: String): List<StructureHotspot> = when (placeId) {
        FLAGSHIP_PLACE_ID -> flagshipHotspots
        else -> flagshipHotspots // DEMO: tüm AR ekranlarında örnek noktaları görmek için
    }

    fun findHotspot(placeId: String, hotspotId: String): StructureHotspot? =
        getHotspots(placeId).find { it.id == hotspotId }

    fun getDetail(placeId: String, hotspotId: String): HotspotDetailData {
        if (hotspotId == "kesme_tas") {
            return kesmeTasDetail
        }
        val hotspot = findHotspot(placeId, hotspotId)
        return defaultDetail.copy(
            hotspotId = hotspotId,
            name = hotspot?.name ?: "Yapı Elemanı",
            longDescription = hotspot?.shortDescription
                ?: "Bu yapı elemanı hakkında detaylı bilgiler yakında eklenecektir."
        )
    }

    /** Hotspot verisi temsilî/demo mu (flagship dışındaki tüm mekânlar için). */
    fun isDemoSample(placeId: String): Boolean = placeId != FLAGSHIP_PLACE_ID

    /** Örnek hotspot setinin bağlandığı gerçek katalog mekânı. */
    const val FLAGSHIP_PLACE_ID = "harput-kalesi"
}
