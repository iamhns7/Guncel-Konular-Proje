# Cursor Prompt — Sadece Hotspot Tıklama Detay İçeriği Entegrasyonu

Elimde sadece hotspot'a tıklanınca açılacak içerikleri taşıyan ZIP dosyası var:

hotspot_click_content_package.zip

Bu ZIP, AR sistemini baştan kurmak için değildir.

## Ana görev

Mevcut AR ekranı, mevcut AR noktaları, mevcut hotspot overlay sistemi ve mevcut AR’da Gör butonu zaten projede var kabul edilecek.

Bu görevde sadece şunu yap:

1. Hotspot'a tıklanınca açılan detay ekranını zenginleştir.
2. ZIP içindeki hotspot içerik JSON'larını oku.
3. Detail image görsellerini detay ekranında göster.
4. Features/özellik tablosunu göster.
5. actions alanlarına göre buton/bölüm göster.
6. show3d true ise 3D Modeli Gör butonunu göster.
7. 3D model ekranını basit şekilde bağla.

AR sistemini, referans fotoğrafı, x/y overlay sistemini veya place entegrasyonunu baştan yazma.

## ZIP'i nereye koyacaksın?

ZIP içinde şu klasör var:

hotspot_contents/

Bu klasörü Android projesinde şuraya kopyala:

app/src/main/assets/ar/hotspot_contents/

Son yapı şöyle olmalı:

app/src/main/assets/ar/hotspot_contents/
├── hotspot_contents.json
├── places/
│   ├── harput-kalesi.json
│   ├── harput-kalesi-genis-aci.json
│   ├── ic-kale-ve-kazi-alani.json
│   ├── artuklu-sarnici-ve-zindani.json
│   └── ulu-cami.json
├── detail_images/
│   └── 25 hotspot detay görseli
├── models/
│   ├── wall-section.glb
│   ├── tower-bastion.glb
│   ├── ceramic-jar.glb
│   ├── rock-cut-tunnel.glb
│   └── leaning-minaret.glb
└── docs/
    ├── HOTSPOT_CONTENT_SCHEMA.md
    └── CURSOR_PROMPT_HOTSPOT_ONLY.md

## Mevcut sistem varsayımı

Mevcut hotspot tıklama kodunda elimizde şu iki bilgi olmalı:

placeId
hotspotId

Örnek:

placeId = "harput-kalesi"
hotspotId = "tas_duvar_dokusu"

Bu iki bilgiyle içerik bulunacak.

## İçerik okuma mantığı

Hotspot'a tıklanınca şu dosyayı oku:

ar/hotspot_contents/places/{placeId}.json

Örnek:

ar/hotspot_contents/places/harput-kalesi.json

Sonra JSON içindeki hotspots listesinden:

id == hotspotId

olan hotspot detay içeriğini bul.

Alternatif olarak tek dosyadan okumak istersen:

ar/hotspot_contents/hotspot_contents.json

dosyasını okuyup places içinden placeId ve hotspotId ile eşleştirme yapabilirsin.

## Data class önerisi

Mevcut modelleri bozma. Sadece gerekiyorsa şu modelleri ekle:

data class HotspotContentPlace(
    val placeId: String,
    val title: String,
    val purpose: String?,
    val hotspots: List<HotspotDetailContent>
)

data class HotspotDetailContent(
    val id: String,
    val name: String,
    val contentType: String?,
    val shortDescription: String?,
    val detailDescription: String?,
    val features: List<HotspotFeature>?,
    val actions: HotspotActions?,
    val media: HotspotMedia?,
    val audioText: String?,
    val historyIdea: String?
)

data class HotspotFeature(
    val label: String,
    val value: String
)

data class HotspotActions(
    val show3d: Boolean,
    val showHistory: Boolean,
    val showAudio: Boolean,
    val showMaterialAnalysis: Boolean
)

data class HotspotMedia(
    val detailImage: String?,
    val model3d: String?
)

## Detay ekranı / bottom sheet

Mevcut hotspot tıklama davranışını şu şekilde geliştir:

Hotspot tıklandı
↓
placeId + hotspotId ile içerik JSON'dan bulunur
↓
HotspotDetailBottomSheet veya HotspotDetailDialog açılır
↓
Zengin içerikler gösterilir

Detay ekranında şu alanlar gösterilmeli:

1. name
2. media.detailImage varsa görsel
3. shortDescription
4. detailDescription
5. features listesi
6. showMaterialAnalysis true ise Malzeme Analizi bölümü
7. showHistory true ise Geçmişi Gör bölümü ve historyIdea
8. showAudio true ise Sesli Anlatım bölümü ve audioText
9. show3d true ve media.model3d varsa 3D Modeli Gör butonu

## Detail image yükleme

JSON'da örnek:

"media": {
  "detailImage": "detail_images/harput-kalesi-tas_duvar_dokusu.jpg"
}

Bu asset path'e karşılık gelir:

ar/hotspot_contents/detail_images/harput-kalesi-tas_duvar_dokusu.jpg

Yani `media.detailImage` değerinin başına:

ar/hotspot_contents/

ekleyerek assets içinden yükleyebilirsin.

Tam path:

ar/hotspot_contents/detail_images/harput-kalesi-tas_duvar_dokusu.jpg

## 3D model

JSON'da örnek:

"actions": {
  "show3d": true
},
"media": {
  "model3d": "models/wall-section.glb"
}

Bu durumda detay ekranında:

3D Modeli Gör

butonu göster.

Model asset path:

ar/hotspot_contents/models/wall-section.glb

İlk aşamada gerçek GLB viewer zor gelirse basit ModelViewerScreen oluştur:

- Başlık
- Model dosya adı
- “Bu model temsilî 3D demo modelidir.” açıklaması
- Varsa detail image

Daha sonra gerçek GLB viewer WebView + model-viewer veya native SceneView/Filament ile bağlanabilir.

## Önemli kısıt

Bu görevde şunları yapma:

- AR sistemi baştan kurma
- Referans fotoğrafları değiştirme
- x/y hotspot koordinat sistemini değiştirme
- ARCore image tracking kurma
- AR’da Gör butonunu yeniden yazma
- Place detay ekranı sistemini baştan bozma

Sadece mevcut hotspot tıklama olayına zengin detay içeriklerini bağla.

## Test

Önce şu hotspot ile test et:

placeId = harput-kalesi
hotspotId = tas_duvar_dokusu

Beklenen:

- Hotspot tıklanınca detay açılır
- Başlık: Taş Duvar Dokusu
- Detay görseli gelir
- Kısa açıklama gelir
- Detay açıklama gelir
- Features listesi görünür
- Malzeme Analizi bölümü görünür
- 3D Modeli Gör butonu görünür
- Butona basınca model ekranı açılır

Sonra şunları test et:

placeId = ulu-cami
hotspotId = egri_minare

placeId = ic-kale-ve-kazi-alani
hotspotId = seramik_kap_kup

placeId = artuklu-sarnici-ve-zindani
hotspotId = kaya_oyma_yuzey

placeId = harput-kalesi-genis-aci
hotspotId = ana_kale_kutlesi

## Beklenen final

Mevcut AR ekranında hotspot'a tıklanınca artık basit açıklama yerine zengin detay ekranı açılacak.

Bu ekran:
- detay görseli
- açıklama
- özellik tablosu
- malzeme/geçmiş/ses bölümleri
- 3D Modeli Gör butonu

gösterecek.
