package com.tayyipgunay.harputarguide.data.asset

enum class FaqIconType {
    Castle,
    Ticket,
    Clock,
    Materials
}

data class FaqEntry(
    val id: String,
    val question: String,
    val answer: String,
    val iconType: FaqIconType
)

object SampleFaqs {
    const val locationName = "Kale Surları"

    val entries = listOf(
        FaqEntry(
            id = "when_built",
            question = "Kale Surları ne zaman yapıldı?",
            answer = "Kale Surları'nın büyük bölümü 12. yüzyılda Artuklular döneminde inşa edilmiştir. Zaman içinde onarımlar ve eklemelerle günümüze ulaşmıştır.",
            iconType = FaqIconType.Castle
        ),
        FaqEntry(
            id = "entry_fee",
            question = "Giriş ücretli mi?",
            answer = "Bu demo sürümünde giriş bilgisi hazır veri olarak gösterilmektedir. Güncel ziyaret ve ücret bilgileri için resmi kaynaklardan kontrol edilmesi önerilir.",
            iconType = FaqIconType.Ticket
        ),
        FaqEntry(
            id = "visit_time",
            question = "En uygun ziyaret saati ne?",
            answer = "Harput'u gezmek için sabah erken saatler veya gün batımına yakın zamanlar daha uygundur. Bu saatlerde ışık daha yumuşak olur ve manzara daha iyi görülebilir.",
            iconType = FaqIconType.Clock
        ),
        FaqEntry(
            id = "materials",
            question = "Burada hangi malzemeler kullanıldı?",
            answer = "Kale Surları'nda ağırlıklı olarak yörede bulunan taş malzemeler, kesme taş yüzeyler ve harç bağlantıları kullanılmıştır.",
            iconType = FaqIconType.Materials
        )
    )

    val defaultEntryId: String = entries.first().id

    fun findById(id: String): FaqEntry? = entries.find { it.id == id }
}
