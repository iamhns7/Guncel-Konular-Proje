package com.tayyipgunay.harputarguide.data.asset

enum class FaqIconType {
    Castle,
    Ticket,
    Clock,
    Materials,
    History,
    Excavation,
    Location,
    Legend,
    Info
}

enum class FaqTier {
    Featured,
    More
}

data class FaqEntry(
    val id: String,
    val question: String,
    val answer: String,
    val iconType: FaqIconType,
    val tier: FaqTier = FaqTier.More
)

object SampleFaqs {
    const val locationName = "Harput Kalesi"

    val entries = listOf(
        FaqEntry(
            id = "when_built",
            question = "Harput Kalesi ne zaman yapıldı?",
            answer = "Harput Kalesi'nin ana kuruluşu genel olarak MÖ 8. yüzyılda Urartu dönemine dayandırılır. Ancak kale tek bir dönemin eseri değildir. Urartulardan sonra Pers, Roma, Bizans, Artuklu, Selçuklu, Akkoyunlu ve Osmanlı dönemlerinde kullanılmış, onarılmış ve yeni yapılarla genişlemiştir.",
            iconType = FaqIconType.Castle,
            tier = FaqTier.Featured
        ),
        FaqEntry(
            id = "why_important",
            question = "Harput Kalesi neden önemlidir?",
            answer = "Harput Kalesi yalnızca bir askerî savunma yapısı değildir. İçinde yaşam alanları, sarnıçlar, ibadet yapıları, atölyeler ve mahalle dokusu bulunan çok katmanlı bir kale-kenttir. Bu yüzden Harput'un tarihini anlamak için kaleyi sadece surlar olarak değil, eski bir şehir merkezi olarak görmek gerekir.",
            iconType = FaqIconType.History
        ),
        FaqEntry(
            id = "who_used",
            question = "Harput Kalesi kimler tarafından kullanıldı?",
            answer = "Harput Kalesi tarih boyunca birçok medeniyetin hâkimiyetinde kalmıştır. Urartular, Persler, Romalılar, Bizanslılar, Artuklular, Selçuklular, Akkoyunlular, Safeviler ve Osmanlılar kalede iz bırakmıştır. Bu yüzden kalede farklı dönemlere ait mimari ve arkeolojik katmanlar bulunur.",
            iconType = FaqIconType.History
        ),
        FaqEntry(
            id = "walls_when",
            question = "Kale surları ne zaman yapıldı?",
            answer = "Kale surlarını tek bir tarihe bağlamak doğru değildir. Savunma sisteminin temeli Urartu dönemine kadar uzanır. Bugün görülen bazı sur, burç ve onarım izleri ise özellikle Artuklu ve Osmanlı dönemleriyle ilişkilidir. Yani Harput Kalesi'nin surları çok dönemli bir yapıdır.",
            iconType = FaqIconType.Castle
        ),
        FaqEntry(
            id = "sut_kalesi_legend",
            question = "Neden \"Süt Kalesi\" deniyor?",
            answer = "\"Süt Kalesi\" adı tarihî bir inşa kaydından değil, halk arasında anlatılan bir efsaneden gelir. Rivayete göre kalenin yapımı sırasında kuraklık yaşanmış ve harçta su yerine süt kullanılmıştır. Bu anlatı kesin tarihî bilgi değil, Harput'un yerel kültüründe yaşayan bir efsanedir.",
            iconType = FaqIconType.Legend,
            tier = FaqTier.Featured
        ),
        FaqEntry(
            id = "materials",
            question = "Burada hangi malzemeler kullanıldı?",
            answer = "Harput Kalesi'nde dönemlere göre farklı taş malzemeler kullanılmıştır. Yapıda kesme taş, kaba yonu taş, moloz taş ve bazı bölümlerde kalker taşı görülür. Sarnıç ve onarım bölümlerinde ise geleneksel yapılarda kullanılan horasan harcı gibi bağlayıcı malzemelerden yararlanılmıştır.",
            iconType = FaqIconType.Materials,
            tier = FaqTier.Featured
        ),
        FaqEntry(
            id = "military_use",
            question = "Kale sadece askerî amaçla mı kullanıldı?",
            answer = "Hayır. Harput Kalesi savunma amacıyla yapılmış olsa da zamanla içinde günlük yaşamın sürdüğü bir yerleşim alanına dönüşmüştür. Kazılarda evler, atölyeler, sarnıçlar, ibadet alanları ve mahalle yapısına ait izler bulunmuştur. Bu nedenle kale aynı zamanda eski Harput yaşamının merkezlerinden biridir.",
            iconType = FaqIconType.History,
            tier = FaqTier.Featured
        ),
        FaqEntry(
            id = "excavations_done",
            question = "Harput Kalesi'nde kazı yapıldı mı?",
            answer = "Evet. Harput Kalesi'nde 2005 yılından itibaren arkeolojik kazılar yapılmıştır. Kazılar, kalenin yalnızca askerî bir yapı olmadığını; içinde Osmanlı mahalle dokusu, sarnıçlar, üretim alanları ve farklı dönemlere ait buluntular bulunduğunu göstermiştir.",
            iconType = FaqIconType.Excavation
        ),
        FaqEntry(
            id = "excavation_finds",
            question = "Kazılarda neler bulundu?",
            answer = "Kazılarda sarnıçlar, seramik parçaları, sikkeler, cam bilezik parçaları, ok uçları, metal objeler, atölye izleri ve günlük yaşama ait çeşitli buluntular ortaya çıkarılmıştır. Bu buluntular Harput Kalesi'nde Urartu'dan Osmanlı'ya kadar uzun süreli bir yaşam olduğunu gösterir.",
            iconType = FaqIconType.Excavation
        ),
        FaqEntry(
            id = "location",
            question = "Harput Kalesi nerede?",
            answer = "Harput Kalesi, Elazığ şehir merkezinin kuzeydoğusunda bulunan tarihî Harput Mahallesi'nde yer alır. Yüksek ve kayalık bir noktaya kurulduğu için hem savunma hem de çevreyi gözetleme açısından stratejik bir konuma sahiptir.",
            iconType = FaqIconType.Location
        ),
        FaqEntry(
            id = "name_meaning",
            question = "Harput adı ne anlama gelir?",
            answer = "Harput adının \"Taş Kale\" anlamıyla açıklandığı kabul edilir. Bu anlam, bölgenin kayalık yapısı ve kalenin güçlü savunma konumuyla ilişkilidir. Harput tarih boyunca farklı adlarla da anılmıştır.",
            iconType = FaqIconType.Info
        ),
        FaqEntry(
            id = "inner_outer_castle",
            question = "İç kale ve dış kale ne demek?",
            answer = "İç kale, kalenin daha korunaklı ve merkezî bölümüdür. Dış kale ise yerleşimi çevreleyen savunma hattını ifade eder. Günümüze iç kaleye ait kalıntılar daha belirgin şekilde ulaşmıştır; dış surların ise bazı bölümleri zamanla tahrip olmuştur.",
            iconType = FaqIconType.Castle
        ),
        FaqEntry(
            id = "why_high_location",
            question = "Harput Kalesi neden yüksek bir yere yapılmış?",
            answer = "Kalenin yüksek ve kayalık bir alana yapılmasının temel sebebi savunmadır. Bu konum sayesinde çevre daha kolay kontrol edilir, yaklaşan tehlikeler önceden fark edilir ve saldırılara karşı doğal bir koruma sağlanır. Bu yüzden Harput Kalesi stratejik bir noktaya kurulmuştur.",
            iconType = FaqIconType.Location
        ),
        FaqEntry(
            id = "cisterns",
            question = "Harput Kalesi'nde sarnıç var mı?",
            answer = "Evet. Harput Kalesi'nde su ihtiyacını karşılamak için yapılmış sarnıçlar vardır. Bu sarnıçlar, kalede uzun süreli yaşam ve savunma için büyük önem taşımıştır. Bazı bölümlerin zamanla depo veya zindan gibi farklı amaçlarla kullanıldığı da düşünülmektedir.",
            iconType = FaqIconType.Castle
        ),
        FaqEntry(
            id = "dungeon",
            question = "Zindan bölümü gerçekten zindan mıydı?",
            answer = "Zindan olarak bilinen bölümün ilk işlevinin su sarnıcı veya depo olduğu düşünülür. Daha sonraki dönemlerde bazı alanlar zindan olarak da kullanılmış olabilir. Bu yüzden bu bölümü yalnızca hapishane olarak değil, zaman içinde işlevi değişmiş bir yapı olarak anlatmak daha doğrudur.",
            iconType = FaqIconType.Info
        ),
        FaqEntry(
            id = "belek_gazi",
            question = "Belek Gazi'nin Harput Kalesi ile bağlantısı nedir?",
            answer = "Belek Gazi, Harput tarihinde önemli bir Artuklu hükümdarıdır. Haçlı Seferleri döneminde bazı önemli esirlerin Harput Kalesi'nde tutulduğu aktarılır. Bu yüzden Harput Kalesi, Artuklu tarihi ve Belek Gazi anlatılarıyla da ilişkilendirilir.",
            iconType = FaqIconType.History
        ),
        FaqEntry(
            id = "harput_relief",
            question = "Harput Kabartması nedir?",
            answer = "Harput Kabartması, Harput çevresinde bulunan önemli bir taş kabartmadır. Bir kale kuşatması sahnesini betimlediği düşünülür. Bu eser, Harput'un erken dönem tarihini anlamak açısından değerli bir arkeolojik bulgu olarak kabul edilir.",
            iconType = FaqIconType.Excavation
        ),
        FaqEntry(
            id = "unesco",
            question = "Harput Kalesi UNESCO listesinde mi?",
            answer = "Harput Tarihi Kenti, UNESCO Dünya Mirası Geçici Listesi'nde yer almaktadır. Burada dikkat edilmesi gereken nokta şudur: Harput, kalıcı Dünya Miras Listesi'nde değil, aday niteliğindeki geçici listededir.",
            iconType = FaqIconType.Info
        ),
        FaqEntry(
            id = "secret_passages",
            question = "Kalede gizli geçit var mı?",
            answer = "Harput Kalesi'nde gizli geçitlerle ilgili çalışmalar ve tespitler vardır. Bu geçitlerin savunma, kaçış veya suya ulaşım gibi amaçlarla kullanılmış olabileceği düşünülür. Ancak bu alanların tamamı her zaman ziyarete açık olmayabilir.",
            iconType = FaqIconType.Castle
        ),
        FaqEntry(
            id = "restoration",
            question = "Kalede restorasyon yapıldı mı?",
            answer = "Evet. Harput Kalesi'nde farklı dönemlerde restorasyon ve koruma çalışmaları yapılmıştır. Özellikle surlar, sarnıçlar, zindan olarak bilinen alanlar ve bazı iç kale bölümleri üzerinde çalışmalar yürütülmüştür. Ama bazı alanlarda kazı ve restorasyon hâlâ dönemsel olarak sürebilir.",
            iconType = FaqIconType.Castle
        ),
        FaqEntry(
            id = "ottoman_life",
            question = "Osmanlı döneminde kalede yaşam var mıydı?",
            answer = "Evet. Harput Kalesi'nde Osmanlı döneminde de yaşam devam etmiştir. Kazılar, kalede Osmanlı mahalle dokusuna ait evler, günlük kullanım eşyaları ve üretim izleri bulunduğunu göstermektedir. Bu durum kalenin Osmanlı döneminde de yerleşim alanı olarak kullanıldığını ortaya koyar.",
            iconType = FaqIconType.History
        ),
        FaqEntry(
            id = "when_decline",
            question = "Harput Kalesi'nde yaşam ne zaman azaldı?",
            answer = "Harput'taki yerleşim zamanla bugünkü Elazığ şehir merkezine doğru kaymıştır. Özellikle 19. yüzyıl sonları ve 20. yüzyıl başlarında eski Harput yerleşimi önemini kaybetmeye başlamıştır. Bu süreçte kale içindeki yaşam da giderek azalmıştır.",
            iconType = FaqIconType.History
        ),
        FaqEntry(
            id = "entry_fee",
            question = "Harput Kalesi'ne giriş ücretli mi?",
            answer = "Güncel resmî bilgilere göre Harput Kalesi ücretsiz ziyaret edilebilen alanlar arasında gösterilmektedir. Ancak ücret bilgisi zamanla değişebileceği için ziyaret öncesi resmî kaynaklardan kontrol edilmesi önerilir.",
            iconType = FaqIconType.Ticket
        ),
        FaqEntry(
            id = "visit_time",
            question = "Ziyaret için en uygun saat nedir?",
            answer = "Yaz aylarında sabah erken saatler veya akşamüstü daha uygundur. Çünkü öğle saatlerinde sıcaklık artabilir. Fotoğraf çekmek ve manzarayı daha iyi görmek için gün batımına yakın saatler tercih edilebilir.",
            iconType = FaqIconType.Clock
        ),
        FaqEntry(
            id = "opening_hours",
            question = "Harput Kalesi her gün açık mı?",
            answer = "Harput Kalesi genel olarak ziyaret edilebilir bir tarihî alandır. Ancak kazı, restorasyon, güvenlik veya resmî düzenlemeler nedeniyle bazı bölümler dönemsel olarak kapalı olabilir. Bu nedenle ziyaret saatleri ve açık alan bilgisi güncellenebilir tutulmalıdır.",
            iconType = FaqIconType.Clock
        )
    )

    val featuredEntries: List<FaqEntry> = entries.filter { it.tier == FaqTier.Featured }
    val moreEntries: List<FaqEntry> = entries.filter { it.tier == FaqTier.More }

    val defaultEntryId: String = featuredEntries.first().id

    fun findById(id: String): FaqEntry? = entries.find { it.id == id }
}
