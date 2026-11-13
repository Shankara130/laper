package com.example.laper.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import com.example.laper.R
import com.example.laper.adapter.MenuAdapter
import com.example.laper.data.DummyData
import com.example.laper.databinding.ActivityRestaurantDetailBinding
import com.example.laper.model.Restaurant
import com.example.laper.util.rating
import com.example.laper.util.safeStart

/**
 * Profil warung + menunya. Menerima objek Restaurant (explicit + Serializable).
 * Implicit intents: ACTION_VIEW geo: (lokasi) dan ACTION_DIAL (telepon).
 */
class RestaurantDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityRestaurantDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySystemBarInsets(binding.root)

        @Suppress("DEPRECATION")
        val restaurant = intent.getSerializableExtra("restaurant") as? Restaurant
            ?: DummyData.restaurants.first()

        val headerColor = Color.parseColor(restaurant.colorHex)
        binding.header.background = GradientDrawable().apply {
            orientation = GradientDrawable.Orientation.TOP_BOTTOM
            colors = intArrayOf(headerColor, darken(headerColor))
        }

        binding.tvAvatar.text = restaurant.name.firstOrNull()?.toString().orEmpty()
        binding.tvName.text = restaurant.name
        binding.tvRating.text = rating(restaurant.rating)
        binding.tvMeta.text = "· ±${restaurant.etaMinutes} menit"
        binding.tvDesc.text = restaurant.description
        binding.tvOpenHours.text = getString(R.string.open_hours, restaurant.openHours)
        binding.tvMenuTitle.text = getString(R.string.menu_at_warung, restaurant.name)

        binding.btnBack.setOnClickListener { finish() }

        // Implicit intent ACTION_VIEW geo:: buka peta.
        binding.btnLocation.setOnClickListener {
            val geo = Uri.parse(
                "geo:${restaurant.lat},${restaurant.lng}?q=${restaurant.lat},${restaurant.lng}(${restaurant.name})"
            )
            safeStart(Intent(Intent.ACTION_VIEW, geo), "Aplikasi peta tidak ditemukan")
        }

        // Implicit intent ACTION_DIAL: buka dialer dengan nomor warung.
        binding.btnCall.setOnClickListener {
            safeStart(
                Intent(Intent.ACTION_DIAL, Uri.parse("tel:${restaurant.phone}")),
                "Aplikasi telepon tidak ditemukan"
            )
        }

        binding.rvMenu.adapter = MenuAdapter(DummyData.menuByRestaurant(restaurant.id))
    }

    private fun darken(color: Int): Int {
        val r = (Color.red(color) * 0.75f).toInt().coerceIn(0, 255)
        val g = (Color.green(color) * 0.75f).toInt().coerceIn(0, 255)
        val b = (Color.blue(color) * 0.75f).toInt().coerceIn(0, 255)
        return Color.rgb(r, g, b)
    }
}
