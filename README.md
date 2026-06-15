1. Proje Başlığı
Harput Akıllı AR Tur Rehberi (Harput Smart AR Tour Guide)

2. Proje Özeti
Harput Akıllı AR Tur Rehberi, kullanıcıların Harput’un tarihi dokusunu mobil cihazları üzerinden, Artırılmış Gerçeklik (AR) teknolojisiyle keşfetmelerini sağlayan Android tabanlı bir kültürel miras rehberidir. İnternet bağlantısının kısıtlı olabileceği durumlar göz önüne alınarak statik içeriklerin (JSON/Asset) ve kullanıcı verilerinin lokalde yönetildiği bu proje; kullanıcılara yapıların geçmiş rekonstrüksiyonlarını görme, tarihi detayları (hotspot) inceleme, kitabeleri OCR ile çevirme ve sesli rehber dinleme imkanı sunar.

3. Problem Tanımı
Harput gibi köklü bir geçmişe sahip tarihi alanları ziyaret eden turistler, genellikle statik ve aşınmış bilgilendirme tabelalarıyla yetinmek zorunda kalmaktadır. Ziyaretçilerin her zaman fiziksel bir rehber bulma imkanı olmaması, eski kitabelerin okunamaması ve tarihi yapıların yıkılmış bölümlerinin hayal edilmesindeki zorluklar, kültürel mirasın tam anlamıyla anlaşılamamasına yol açmaktadır. Bu durum, tarihi alan gezilerini interaktif ve eğitici bir deneyim olmaktan uzaklaştırmaktadır.

4. Amaçlar
Tarihi Harput bölgesindeki yapıları dijital bir katmanla zenginleştirerek interaktif bir gezi deneyimi sunmak.

Yıkılmış veya zarar görmüş yapıların yapay zeka ile üretilmiş geçmiş temsillerini AR aracılığıyla gerçek dünyada görselleştirmek.

OCR teknolojisi ile eski metinleri ve kitabeleri anlık olarak günümüz dillerine çevirerek erişilebilirliği artırmak.

Oyunlaştırma (gezilen yerleri takip etme, favorilere ekleme) ile kullanıcıların bölgedeki ziyaret süresini ve motivasyonunu maksimize etmek.

5. Temel Kavramlar
Mobil Artırılmış Gerçeklik (Mobile AR): Cihaz kamerası ve sensörleri kullanılarak gerçek dünya görüntüsü üzerine dijital nesnelerin ve bilgilerin bindirilmesi.

OCR (Optik Karakter Tanıma): Kitabe ve tabelalardaki metinlerin kamera aracılığıyla algılanıp dijital metne dökülmesi.

Hotspot: AR ekranında mimari detayların (kesme taş, kemer vb.) üzerinde beliren tıklanabilir dijital bilgi noktaları.

Lokal Veri Yönetimi: Kullanıcıya özel gezi verilerinin sunucuya ihtiyaç duymadan cihazın kendi belleğinde tutulması.

6. Kullanılan Teknolojiler
Platform ve Dil: Android, Kotlin

AR ve Kamera Arayüzü: ARCore / Sceneview (Artırılmış gerçeklik entegrasyonu)

Lokal Veri Yönetimi: Room Database, DataStore (Favoriler, gezi takibi ve ayarlar için)

Görüntü İşleme / Çeviri: Google ML Kit (Metin algılama/OCR)

Yapay Zeka (Planlanan/Gelişmiş Sürüm): Google Gemini API (Sanal rehber entegrasyonu)

Mimari: MVVM (Model-View-ViewModel)

7. Proje Kapsamı
Proje, Harput bölgesindeki seçilmiş pilot noktaları (Kale Surları, Harput Kalesi, Ulu Cami, Şefik Gül Kültür Evi vb.) kapsamaktadır.
Dahil Olan Özellikler:

Nokta seçimi ve basit navigasyon yönlendirmesi.

4 Farklı AR Modu: Bilgi Katmanı, Geçmişi Gör (Slider ile overlay), Yapı Bilgisi (Pre-defined hotspotlar), Kitabe Oku (OCR).

Sesli Rehber ve Sanal Rehber (Hazır soru-cevap asistanı).

Kullanıcı ilerleme takibi (Gezilen Noktalar, Favoriler).
Kapsam Dışı:

Cihaz üzerinde anlık AI görsel üretimi (performans ve stabilite için görseller önceden üretilip JSON/Asset olarak projeye dahil edilmiştir).

Kapsamlı bir bulut (backend) veri senkronizasyonu (İlk sürüm tamamen lokal çalışır).

8. Beklenen Çıktılar
Kullanıcıların Harput'ta cihaz kamerasını kullanarak tarihle etkileşime girebileceği, yüksek performanslı yerel (native) bir Android uygulaması.

Tarihi yapıların temsili rekonstrüksiyonlarının bulunduğu dijital bir envanter.

Turizm alanında kullanılabilecek, backend maliyeti gerektirmeyen, ölçeklenebilir ve cihaz donanımını verimli kullanan bir akıllı rehber altyapısı.

9. Katkıda Bulunanlar
Bu proje, Fırat Üniversitesi Yazılım Mühendisliği öğrencileri tarafından geliştirilmektedir:
- Hasan Sido 225541601
- Tayyıp Güney 215541009

10. Kaynaklar
Elazığ İl Kültür ve Turizm Müdürlüğü Harput Arşivi

Kotlin ve Android Geliştirici Dokümantasyonları

Google ARCore ve Sceneview Dokümantasyonları

Google ML Kit OCR Rehberi

11. Anahtar Kelimeler
Android, Kotlin, ARCore, Artırılmış Gerçeklik, Harput Kalesi, Kültürel Miras, Turizm Teknolojileri, Room Database, ML Kit, Yazılım Mühendisliği, Fırat Üniversitesi.
