from PIL import Image
import os
import shutil

DESKTOP_SRC = r"C:\Users\gunay\OneDrive\Masaüstü\güncel konular fotolar"
DOCS_BASE = r"C:\Users\gunay\AndroidStudioProjects\HarputARGuide\docs\design\3d-detail-screens"
OUT_CROPS = os.path.join(DOCS_BASE, "crops")
OUT_VIEWPORT = r"C:\Users\gunay\AndroidStudioProjects\HarputARGuide\app\src\main\assets\ar\hotspot_contents\viewport_previews"

# Masaüstü 1.png..5.png -> proje referans adları
SPLIT_MAP = [
    ("1.png", "harput-kalesi-3d-screens.png", 3, [
        ("harput-kalesi", "ana_burc"),
        ("harput-kalesi", "tas_duvar_dokusu"),
        ("harput-kalesi", "sag_burc"),
    ]),
    ("2.png", "harput-kalesi-genis-aci-3d-screens.png", 2, [
        ("harput-kalesi-genis-aci", "orta_burc"),
        ("harput-kalesi-genis-aci", "sag_burc"),
    ]),
    ("3.png", "ic-kale-kazi-alani-3d-screens.png", 2, [
        ("ic-kale-ve-kazi-alani", "seramik_kap_kup"),
        ("ic-kale-ve-kazi-alani", "tas_duvar"),
    ]),
    ("4.png", "artuklu-sarnici-zindan-3d-screens.png", 3, [
        ("artuklu-sarnici-ve-zindani", "kaya_oyma_yuzey"),
        ("artuklu-sarnici-ve-zindani", "basamaklar"),
        ("artuklu-sarnici-ve-zindani", "tunel_gecidi"),
    ]),
    ("5.png", "ulu-cami-3d-screens.png", 3, [
        ("ulu-cami", "egri_minare"),
        ("ulu-cami", "tugla_orgu"),
        ("ulu-cami", "moloz_tas_duvar"),
    ]),
]

Y0, Y1 = 98, 638
SIDE = 22
GAP = 18

os.makedirs(OUT_CROPS, exist_ok=True)
os.makedirs(OUT_VIEWPORT, exist_ok=True)
os.makedirs(DOCS_BASE, exist_ok=True)

for desktop_name, docs_name, cols, hotspots in SPLIT_MAP:
    desktop_path = os.path.join(DESKTOP_SRC, desktop_name)
    docs_path = os.path.join(DOCS_BASE, docs_name)
    shutil.copy2(desktop_path, docs_path)

    src = Image.open(desktop_path).convert("RGB")
    w, _ = src.size
    row_w = w - 2 * SIDE
    phone_w = (row_w - GAP * (cols - 1)) // cols

    for i, (place_id, hotspot_id) in enumerate(hotspots):
        x0 = SIDE + i * (phone_w + GAP)
        x1 = x0 + phone_w
        phone = src.crop((x0, Y0, x1, Y1))

        phone_name = f"{place_id}__{hotspot_id}__phone.png"
        phone.save(os.path.join(OUT_CROPS, phone_name))

        ph_w, ph_h = phone.size
        vx0 = int(ph_w * 0.06)
        vx1 = int(ph_w * 0.58)
        vy0 = int(ph_h * 0.18)
        vy1 = int(ph_h * 0.66)
        viewport = phone.crop((vx0, vy0, vx1, vy1))

        asset_name = f"{place_id}-{hotspot_id}.png"
        viewport.save(os.path.join(OUT_VIEWPORT, asset_name), optimize=True)
        print(f"saved {asset_name} {viewport.size}")
