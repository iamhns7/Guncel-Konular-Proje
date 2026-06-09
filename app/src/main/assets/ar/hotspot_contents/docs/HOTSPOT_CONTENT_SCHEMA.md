# Hotspot Click Content Package

Bu paket sadece hotspot'a tıklanınca açılacak içerikler içindir.

## Bu pakette VAR

- Hotspot detay JSON'ları
- 25 hotspot için detay açıklamaları
- Features / özellik tabloları
- Detail image referansları
- Detail image dosyaları
- 3D model referansları
- 5 temsilî GLB model dosyası
- Cursor entegrasyon promptu

## Bu pakette YOK

- AR referans fotoğrafları
- ARCore kurulumu
- Hotspot x/y overlay sistemi
- AR destekli placeId kurulumu
- AR’da Gör butonu kurulumu

Çünkü bunların projede zaten mevcut olduğu varsayılır.

## Önerilen asset hedefi

Paket içindeki:

`hotspot_contents/`

klasörü Android projesinde şuraya kopyalanabilir:

`app/src/main/assets/ar/hotspot_contents/`

Hedef yapı:

```text
app/src/main/assets/ar/hotspot_contents/
├── hotspot_contents.json
├── places/
├── detail_images/
├── models/
└── docs/
```

## JSON kullanım mantığı

Hotspot'a tıklanınca mevcut sistem zaten şu bilgileri bilir:

- placeId
- hotspotId

Bu paketle yapılacak şey:

```text
placeId + hotspotId
↓
hotspot içerik JSON'unda ilgili kaydı bul
↓
detay ekranını aç
```

Örnek:

```text
placeId = harput-kalesi
hotspotId = tas_duvar_dokusu
```

Okunacak dosya:

```text
ar/hotspot_contents/places/harput-kalesi.json
```

Sonra `hotspots` listesinden:

```text
id == tas_duvar_dokusu
```

olan kayıt bulunur.

## Gösterilecek alanlar

- name
- shortDescription
- detailDescription
- features
- media.detailImage
- actions.show3d
- actions.showHistory
- actions.showAudio
- actions.showMaterialAnalysis
- media.model3d
- audioText
- historyIdea
