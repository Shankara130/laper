#!/bin/bash
# Unduh 16 foto menu dari Wikimedia Commons (CC BY-SA) ke drawable-nodpi.
# Rate-limit aman: jeda 2.5 detik antar permintaan, retry sekali saat 429.
set -u
DEST="/Volumes/RX7 128GB SATA/Project/tugas-bp3/app/src/main/res/drawable-nodpi"
mkdir -p "$DEST"
UA="LaperStudentApp/1.0 (tugas akhir BP3; mailto:student@example.edu)"

declare -a DISHES=(
  "food_nasi_goreng:Nasi goreng"
  "food_nasi_uduk:Nasi uduk"
  "food_rendang:Rendang"
  "food_mie_ayam:Mie ayam"
  "food_bakso:Bakso"
  "food_ayam_geprek:Ayam geprek"
  "food_sate_ayam:Sate ayam"
  "food_soto_ayam:Soto ayam"
  "food_gado_gado:Gado gado"
  "food_martabak:Martabak telur"
  "food_pisang_goreng:Pisang goreng"
  "food_pempek:Pempek"
  "food_es_cendol:Es cendol"
  "food_es_teh:Es teh manis"
  "food_kopi_susu:Kopi susu gula aren"
  "food_roti_bakar:Roti bakar"
)

for entry in "${DISHES[@]}"; do
  name="${entry%%:*}"
  term="${entry#*:}"
  query=$(python3 - "$term" <<'PY'
import sys, urllib.parse
term = sys.argv[1]
print(urllib.parse.quote(f"filetype:bitmap {term}"))
PY
)
  api="https://commons.wikimedia.org/w/api.php?action=query&format=json&generator=search&gsrnamespace=6&gsrlimit=4&prop=imageinfo&iiprop=url%7Cmime&iiurlwidth=800&gsrsearch=${query}"
  url=$(curl -s -A "$UA" "$api" | python3 -c '
import sys, json
try:
    data = json.load(sys.stdin)
    pages = data.get("query", {}).get("pages", {})
    # urutkan sesuai indeks relevansi (index terkecil = hasil teratas)
    for p in sorted(pages.values(), key=lambda p: p.get("index", 99)):
        info = p.get("imageinfo", [{}])[0]
        mime = info.get("mime", "")
        if mime in ("image/jpeg", "image/png") and info.get("thumburl"):
            print(info["thumburl"])
            break
except Exception:
    pass
')
  if [ -z "$url" ]; then
    echo "MISS-URL $name ($term)"
    continue
  fi
  url="${url%%\?*}"   # buang sufiks ?utm_...
  sleep 2.5
  # tentukan ekstensi dari mime
  ext="jpg"
  case "$url" in *.png) ext="png";; *.jpg|*.jpeg) ext="jpg";; esac
  for attempt in 1 2; do
    code=$(curl -s -A "$UA" -w "%{http_code}" -o "$DEST/$name.$ext" "$url")
    if [ "$code" = "200" ]; then
      echo "OK $name.$ext <- $(basename "$url")"
      break
    elif [ "$code" = "429" ]; then
      echo "RETRY($code) $name"; sleep 5
    else
      echo "FAIL($code) $name <- $url"
      break
    fi
  done
done
echo "--- summary ---"
ls -la "$DEST" | grep food_ || true
