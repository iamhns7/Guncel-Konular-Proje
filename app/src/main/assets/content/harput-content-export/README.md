# Harput İçerik Paketi

Yeni Kotlin projesine kopyalanacak **sadece veri + görseller** (quiz yok, ekran kodu yok).

## Klasör yapısı

```
harput-content-export/
├── manifest.json          # dil ayarları (tr, en)
├── images/                # 19 Harput fotoğrafı (.jpg / .webp)
├── tr/
│   ├── places/meta/       # 19 mekan — liste kartı bilgisi
│   ├── places/detail/     # 19 mekan — uzun açıklama
│   ├── timelines/meta/    # timeline başlıkları
│   └── timelines/story/   # timeline slaytları (events)
└── en/                    # aynı yapı (İngilizce)
```

## Kotlin’e nasıl kopyalanır?

**Seçenek A — assets (önerilen):**
```
app/src/main/assets/harput/
  → bu klasörün tamamını buraya yapıştırın
```

**Seçenek B — görseller drawable:**
```
images/*  →  res/drawable/  (dosya adlarını küçük harf + _ yapın)
JSON      →  assets/harput/tr/... ve assets/harput/en/...
```

## JSON alanları

### `places/meta/{id}.json` — kart / liste
| Alan | Örnek |
|------|--------|
| id | `harput-kalesi` |
| name | `HARPUT KALESI` |
| shortDescription | kısa metin |
| imageAsset | `media/Harput-kalesi.jpg` → sizde `images/Harput-kalesi.jpg` |
| category | `historical` |
| coordinate | latitude, longitude |
| yearOrEra | `12. Yüzyıl` |

### `places/detail/{id}.json` — detay
| Alan | Örnek |
|------|--------|
| id | `harput-kalesi` |
| about | uzun tarih metni |
| audioAsset | (dosya yok, opsiyonel) |
| modelAsset | (dosya yok, opsiyonel) |

### `timelines/meta/tl-{id}.json`
| Alan | Açıklama |
|------|----------|
| id | `tl-harput-kalesi` |
| placeId | bağlı mekan |
| title, description | başlık / özet |

### `timelines/story/tl-{id}.json`
| Alan | Açıklama |
|------|----------|
| events[] | year, title, content, imageAsset |

## Görsel eşlemesi

JSON içinde `imageAsset` veya `imagePath` alanı `media/...` ile başlar.  
Kotlin’de çözüm: `images/` klasöründeki aynı dosya adını kullanın.

Örnek: `"media/Harput-kalesi.jpg"` → `assets/harput/images/Harput-kalesi.jpg`

## 19 mekan ID listesi

`places-index.json` dosyasına bakın.

## Dahil değil

- Quiz soruları
- Okyanus mock verileri
- React / ekran kodu
- Ses (.mp3) ve 3D (.glb) dosyaları (JSON’da referans var, dosya yok)
