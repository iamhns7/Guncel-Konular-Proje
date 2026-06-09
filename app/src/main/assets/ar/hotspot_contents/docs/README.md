# Hotspot Click Content Package

Bu ZIP sadece hotspot'a tıklanınca açılacak içerikleri içerir.

## Kapsam

Bu paket:
- AR sistemini baştan kurmaz.
- Referans fotoğraf içermez.
- x/y overlay entegrasyonu içermez.
- Sadece hotspot detay ekranı için içerik sağlar.

## Projeye kopyalanacak klasör

`hotspot_contents/`

hedef:

`app/src/main/assets/ar/hotspot_contents/`

## Kullanım

Mevcut AR ekranında hotspot'a tıklanınca elindeki:
- placeId
- hotspotId

bilgisiyle ilgili içerik okunur.

Örnek:

`ar/hotspot_contents/places/harput-kalesi.json`

içinden:

`id == tas_duvar_dokusu`

olan kayıt bulunur ve detay ekranında gösterilir.
