package com.example.laper.data

import com.example.laper.R
import com.example.laper.model.MenuItem
import com.example.laper.model.Promo
import com.example.laper.model.Restaurant

/**
 * Seluruh data statis (dummy) aplikasi Laper!
 * 16 menu, 5 warung, 4 promo — semua berkategori dan saling terhubung lewat restaurantId.
 */
object DummyData {

    // ---- Kategori ----

    const val CAT_ALL = "Semua"
    val categories = listOf(
        CAT_ALL, "Nasi & Lauk", "Mie & Bakso", "Ayam", "Sayur", "Camilan", "Minuman"
    )

    // ---- Warung ----

    val restaurants = listOf(
        Restaurant(
            id = 1,
            name = "Warung Bu Sari",
            description = "Masakan rumahan khas Indonesia dengan porsi paling kenyang sekitar kampus.",
            rating = 4.8,
            lat = -6.3627, lng = 106.8252,
            phone = "+6281234567890",
            openHours = "07.00–21.00",
            etaMinutes = 15,
            colorHex = "#C2410C"
        ),
        Restaurant(
            id = 2,
            name = "Kantin Teknik",
            description = "Ayam geprek dan sate ayam paling dicari mahasiswa. Ramai saat jam istirahat!",
            rating = 4.6,
            lat = -6.3645, lng = 106.8269,
            phone = "+6281234567891",
            openHours = "08.00–18.00",
            etaMinutes = 10,
            colorHex = "#3D6B35"
        ),
        Restaurant(
            id = 3,
            name = "Baso Pak Kumis",
            description = "Bakso urat dan mie ayam legendaris sejak 1998. Kuah kaldunya juara.",
            rating = 4.7,
            lat = -6.3601, lng = 106.8240,
            phone = "+6281234567892",
            openHours = "09.00–20.00",
            etaMinutes = 12,
            colorHex = "#8D5B24"
        ),
        Restaurant(
            id = 4,
            name = "Gudeg Mbah Yat",
            description = "Gudeg dan nasi uduk ala Jogja yang merantau ke Depok. Buka pagi, habis siang.",
            rating = 4.5,
            lat = -6.3670, lng = 106.8280,
            phone = "+6281234567893",
            openHours = "06.30–15.00",
            etaMinutes = 20,
            colorHex = "#7C5334"
        ),
        Restaurant(
            id = 5,
            name = "Kedai Kopi Kampus",
            description = "Kopi gula aren, roti bakar, dan tempat nongkrong favorit begadang tugas.",
            rating = 4.9,
            lat = -6.3610, lng = 106.8260,
            phone = "+6281234567894",
            openHours = "10.00–23.00",
            etaMinutes = 8,
            colorHex = "#9A3412"
        )
    )

    // ---- Menu (16 item) ----

    val menu = listOf(
        MenuItem(
            id = 1, name = "Nasi Goreng Spesial",
            description = "Nasi goreng dengan telur mata sapi, suwiran ayam, acar segar, dan kerupuk. Porsi besar dijamin kenyang.",
            price = 18000, category = "Nasi & Lauk", imageRes = R.drawable.food_nasi_goreng,
            restaurantId = 1, rating = 4.9, sold = 1240
        ),
        MenuItem(
            id = 2, name = "Nasi Uduk Komplit",
            description = "Nasi uduk gurih dengan ayam goreng, bihun, telur balado, timun, dan sambal kacang.",
            price = 15000, category = "Nasi & Lauk", imageRes = R.drawable.food_nasi_uduk,
            restaurantId = 4, rating = 4.7, sold = 860
        ),
        MenuItem(
            id = 3, name = "Rendang Daging",
            description = "Rendang daging sapi empuk dimasak 8 jam dengan santan kelapa dan rempah asli Minang. Pedas nikmat.",
            price = 25000, category = "Nasi & Lauk", imageRes = R.drawable.food_rendang,
            restaurantId = 1, rating = 4.9, spicy = true, sold = 640
        ),
        MenuItem(
            id = 4, name = "Mie Ayam Jamur",
            description = "Mie ayam home made dengan topping jamur ayam kecap, pangsit rebus, dan kuah kaldu bening.",
            price = 15000, category = "Mie & Bakso", imageRes = R.drawable.food_mie_ayam,
            restaurantId = 3, rating = 4.8, sold = 1520
        ),
        MenuItem(
            id = 5, name = "Bakso Urat Solo",
            description = "Bakso urat kenyal dalam kuah kaldu sapi panas, lengkap tahu, mie putih, dan sambal terpisah.",
            price = 17000, category = "Mie & Bakso", imageRes = R.drawable.food_bakso,
            restaurantId = 3, rating = 4.7, sold = 980
        ),
        MenuItem(
            id = 6, name = "Ayam Geprek Sambal Bawang",
            description = "Ayam crispy digeprek dengan sambal bawang segar. Level pedas bisa disesuaikan, nasi putih gratis.",
            price = 16000, category = "Ayam", imageRes = R.drawable.food_ayam_geprek,
            restaurantId = 2, rating = 4.9, spicy = true, sold = 2310
        ),
        MenuItem(
            id = 7, name = "Sate Ayam Madura (10 tusuk)",
            description = "Sate ayam bakar bumbu kacang khas Madura dengan lontong, bawang goreng, dan kecap manis.",
            price = 20000, category = "Ayam", imageRes = R.drawable.food_sate_ayam,
            restaurantId = 2, rating = 4.8, sold = 1120
        ),
        MenuItem(
            id = 8, name = "Soto Ayam Lamongan",
            description = "Soto ayam kuah kuning dengan koya gurih, telur rebus, tauge, dan perasan jeruk nipis.",
            price = 15000, category = "Ayam", imageRes = R.drawable.food_soto_ayam,
            restaurantId = 1, rating = 4.6, sold = 750
        ),
        MenuItem(
            id = 9, name = "Gado-Gado Siram",
            description = "Sayuran segar, kentang, telur rebus, dan lontong disiram bumbu kacang kental plus kerupuk.",
            price = 14000, category = "Sayur", imageRes = R.drawable.food_gado_gado,
            restaurantId = 1, rating = 4.5, sold = 430
        ),
        MenuItem(
            id = 10, name = "Martabak Telur Mini",
            description = "Martabak telur mini dengan daging cincang, bawang daun, dan adonan kulit renyah. Cocok buat sharing.",
            price = 22000, category = "Camilan", imageRes = R.drawable.food_martabak,
            restaurantId = 5, rating = 4.8, sold = 520
        ),
        MenuItem(
            id = 11, name = "Pisang Goreng Keju (6 pcs)",
            description = "Pisang goreng crispy dengan topping keju parut melimpah dan susu kental manis.",
            price = 12000, category = "Camilan", imageRes = R.drawable.food_pisang_goreng,
            restaurantId = 5, rating = 4.7, sold = 890
        ),
        MenuItem(
            id = 12, name = "Pempek Kapal Selam",
            description = "Pempek besar isi telur dengan cuko asam pedas, mie sotang, dan timun. Khas Palembang.",
            price = 19000, category = "Camilan", imageRes = R.drawable.food_pempek,
            restaurantId = 4, rating = 4.6, sold = 380
        ),
        MenuItem(
            id = 13, name = "Es Cendol Susu",
            description = "Cendol pandan kenyal dengan susu segar, gula merah asli, dan es serut. Segar maksimal.",
            price = 13000, category = "Minuman", imageRes = R.drawable.food_es_cendol,
            restaurantId = 5, rating = 4.8, sold = 1340
        ),
        MenuItem(
            id = 14, name = "Es Teh Manis Jumbo",
            description = "Es teh manis ukuran jumbo 500 ml. Pendamping wajib semua menu pedas.",
            price = 5000, category = "Minuman", imageRes = R.drawable.food_es_teh,
            restaurantId = 1, rating = 4.9, sold = 3120
        ),
        MenuItem(
            id = 15, name = "Kopi Susu Gula Aren",
            description = "Espresso bold, susu segar, dan gula aren asli tanpa pemanis buatan. Favorit anak kos.",
            price = 15000, category = "Minuman", imageRes = R.drawable.food_kopi_susu,
            restaurantId = 5, rating = 4.9, sold = 1980
        ),
        MenuItem(
            id = 16, name = "Roti Bakar Cokelat Keju",
            description = "Roti bakar mentega dengan lelehan cokelat dan keju cheddar. Disajikan hangat.",
            price = 14000, category = "Camilan", imageRes = R.drawable.food_roti_bakar,
            restaurantId = 5, rating = 4.7, sold = 960
        )
    )

    // ---- Promo ----

    val promos = listOf(
        Promo("LAPERBARU", "Diskon Rp5.000", "Min. belanja Rp25.000 buat pengguna baru", 5000, 25000, "#C2410C"),
        Promo("MAKSIMAL8", "Diskon Rp8.000", "Min. belanja Rp50.000, berlaku semua menu", 8000, 50000, "#3D6B35"),
        Promo("GAJIAN20", "Diskon Rp20.000", "Min. belanja Rp100.000, khusus tanggal tua", 20000, 100000, "#7C5334"),
        Promo("NONGKRONYUK", "Diskon Rp3.000", "Min. belanja Rp15.000, cocok buat ngemil sore", 3000, 15000, "#9A3412")
    )

    // ---- Helper ----

    fun restaurantById(id: Int): Restaurant? = restaurants.firstOrNull { it.id == id }

    fun menuById(id: Int): MenuItem? = menu.firstOrNull { it.id == id }

    fun menuByCategory(category: String): List<MenuItem> =
        if (category == CAT_ALL) menu else menu.filter { it.category == category }

    fun menuByRestaurant(restaurantId: Int): List<MenuItem> =
        menu.filter { it.restaurantId == restaurantId }

    /** Menu terlaris untuk dashboard (urut jumlah terjual, ambil n). */
    fun mostSold(count: Int = 8): List<MenuItem> = menu.sortedByDescending { it.sold }.take(count)

    fun promoByCode(code: String): Promo? = promos.firstOrNull { it.code.equals(code, ignoreCase = true) }
}
