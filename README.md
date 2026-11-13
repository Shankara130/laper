# Laper! 🍜

Aplikasi Android pemesanan makanan warung sekitar kampus — **Tugas Akhir mata kuliah Bahasa Pemrograman 3** (Android, Kotlin).

Dibuat dengan data statis (dummy) berikut 16 foto menu asli dari Wikimedia Commons, fokus pada **UI/UX Material 3**, dan mendemonstrasikan **intent eksplisit + implisit** secara menyeluruh.

| | |
|---|---|
| Bahasa | Kotlin |
| UI | XML Views · Material 3 · ViewBinding · Dark Mode |
| Arsitektur | Activity-based + data layer (SharedPreferences JSON) |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 36 |
| Build | Gradle 9.9.1 · AGP 9.4.0 |

## Akun Demo

| Email | Kata Sandi |
|---|---|
| `demo@student.ac.id` | `bp3123` |

Kode promo yang bisa dicoba di keranjang: `LAPERBARU` (min. Rp25.000), `NONGKRONYUK` (min. Rp15.000), `MAKSIMAL8` (min. Rp50.000), `GAJIAN20` (min. Rp100.000).

## Daftar Halaman (15 layar)

1. **Splash** — routing sesuai sesi login
2. **Login** — autentikasi dummy + session SharedPreferences
3. **Register** — validasi form, akun tersimpan lokal
4. **Beranda (Dashboard)** — banner promo ViewPager2 auto-next, chips kategori, menu terlaris (RV horizontal), daftar warung
5. **Daftar Menu** — RecyclerView 16 item + pencarian + filter kategori
6. **Detail Menu** — foto, stepper jumlah, catatan, tambah ke keranjang
7. **Daftar Warung** — 5 warung sekitar kampus
8. **Detail Warung** — profil warung + menunya, tombol lokasi & telepon
9. **Keranjang** — ubah jumlah, hapus, kode promo, ringkasan biaya
10. **Checkout** — form pengiriman + metode pembayaran + ringkasan
11. **Pesanan Berhasil** — kode pesanan + bagikan
12. **Lacak Pesanan** — timeline status 4 tahap + kartu kurir
13. **Riwayat Pesanan** — pesanan tersimpan (SharedPreferences JSON) + pesan ulang
14. **Profil** — data pengguna, mode gelap, keluar
15. **Tentang** — kredit, email developer, beri ulasan

## Ketertelusuran Syarat Tugas

| Syarat | Implementasi |
|---|---|
| Minimal 10 halaman berbeda | 15 Activity (lihat daftar di atas) |
| Halaman login | `LoginActivity` |
| Halaman register | `RegisterActivity` |
| Dashboard/menu | `MainActivity` |
| List/recycle item (min. 10 item) | `MenuListActivity` — 16 item di `RecyclerView` |
| Detail item (min. 10 item) | `MenuDetailActivity` — semua item punya halaman detail |
| Klik item RecyclerView → detail dengan data | `MenuAdapter` → `MenuDetailActivity` (objek `MenuItem` via `Serializable` extra) |
| Intent eksplisit dengan data | Setiap navigasi antar Activity membawa extra: `MenuItem`, `Restaurant`, `ArrayList<CartItem>`, `Order`, `category`, `promoCode`, dsb. |
| Intent implisit dengan data | `ACTION_VIEW geo:` lokasi warung · `ACTION_DIAL tel:` kurir & warung · `ACTION_SENDTO mailto:` email developer · `ACTION_SEND` bagikan menu/pesanan · `ACTION_VIEW market://` beri ulasan |
| UI/UX + prototyping | Material 3 (light + dark), 16 foto asli, splash screen, empty state, animasi transisi |

## Menjalankan Proyek

```bash
# 1. Pastikan JAVA_HOME mengarah ke JDK 17+ (mis. JBR dari Android Studio)
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"

# 2. Sesuaikan sdk.dir di local.properties (biasanya sudah benar)
# sdk.dir=/Users/<user>/Library/Android/sdk

# 3. Build debug APK
./gradlew assembleDebug
# Hasil: app/build/outputs/apk/debug/app-debug.apk

# 4. Build APK release (signed) — perlu keystore.properties (lihat contoh: keystore.properties.example)
cp keystore.properties.example keystore.properties   # sesuaikan bila perlu
./gradlew assembleRelease
# Hasil: app/build/outputs/apk/release/app-release.apk

# 5. Install ke emulator/perangkat
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

Bisa juga dibuka langsung di Android Studio (Rabbit 1 / 2026.2.1 atau lebih baru, AGP 9.4).

## Struktur Proyek

```
app/src/main/java/com/example/laper/
├── LaperApp.kt              # Inisialisasi global (manager + dark mode)
├── ui/                      # 15 Activity + BaseActivity (insets & transisi)
├── adapter/                 # RecyclerView & ViewPager2 adapters
├── data/                    # DummyData, SessionManager, CartManager, OrderRepository
├── model/                   # MenuItem, Restaurant, Promo, CartItem, Order, User, OrderStatus
└── util/                    # Formatters (rupiah, rating), Extensions

app/src/main/res/
├── layout/                  # 15 activity_*.xml + item_*.xml
├── drawable-nodpi/          # 16 foto menu (Wikimedia Commons)
├── values/ + values-night/  # Tema Material 3 light/dark
└── mipmap-*/                # Ikon launcher adaptif + legacy
```

## Kredit

- Foto menu: [Wikimedia Commons](https://commons.wikimedia.org) — lisensi CC BY-SA (Nasi goreng pattaya, Sate Ayam Al-Huda, Bakso Solo, Mi ayam jamur, Lamb rendang, Gado-gado, Ayam geprek, Martabak telur, Es cendol, Pisang goreng, Soto ayam, Nasi uduk, Pempek, Es teh, Es kopi susu gula aren, Roti bakar).
- Ikon: [Material Symbols](https://fonts.google.com/icons) (Apache 2.0).

## Tim

Mahasiswa BP3 — nama, NIM, kelas (lihat laporan).
