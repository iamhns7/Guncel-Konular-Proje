# AR Kurulum ve Demo Rehberi

## Gereksinimler

- ARCore destekli Android cihaz (opsiyonel; emülatörde fallback mod çalışır)
- Kamera izni (ARCore image tracking için)
- Demo için: bilgisayarda **referans fotoğraf** tam ekran açık

## İçerik yapısı

```
app/src/main/assets/ar/
├── manifest.json
├── images/
│   └── {placeId}-reference.jpg
└── places/
    └── {placeId}.json
```

Referans görsel, uygulamaya gömülen dosya ile **birebir aynı** olmalıdır (PC ekranında açılan dosya).

## Destekli 5 nokta

- `harput-kalesi`
- `harput-kalesi-genis-aci`
- `ic-kale-ve-kazi-alani`
- `artuklu-sarnici-ve-zindani`
- `ulu-cami`

## Çalışma modları

1. **Fallback (öncelikli):** Referans fotoğraf ekranda gösterilir; hotspotlar x/y oranlarına göre fotoğraf sınırları içinde çizilir.
2. **ARCore (opsiyonel):** Kamera referans görseli tanıdığında hotspotlar görsele bindirilir.

## Demo adımları

1. Uygulamada destekli noktadan detay ekranına girin.
2. **AR ile İncele** butonuna basın.
3. **Yapı Bilgisi** modunda hotspotlara dokunun.
4. (ARCore) PC'de aynı referans JPG tam ekran açın ve kamerayı tutun.
