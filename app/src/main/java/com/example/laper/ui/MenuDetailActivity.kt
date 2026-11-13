package com.example.laper.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.example.laper.R
import com.example.laper.data.CartManager
import com.example.laper.data.DummyData
import com.example.laper.databinding.ActivityMenuDetailBinding
import com.example.laper.model.MenuItem
import com.example.laper.util.rating
import com.example.laper.util.rupiah
import com.example.laper.util.safeStart
import com.example.laper.util.toast

/**
 * Detail menu. Menerima objek MenuItem via Intent (explicit + Serializable).
 * Implicit intents di sini: ACTION_SEND (bagikan) dan ACTION_VIEW geo: (lokasi warung).
 */
class MenuDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityMenuDetailBinding
    private lateinit var menu: MenuItem
    private var qty = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySystemBarInsets(binding.root)

        @Suppress("DEPRECATION")
        menu = intent.getSerializableExtra("menu") as? MenuItem
            ?: DummyData.menu.first()

        bindMenu()
        setupActions()
    }

    private fun bindMenu() {
        val warung = DummyData.restaurantById(menu.restaurantId)

        binding.ivMenu.setImageResource(menu.imageRes)
        binding.tvName.text = menu.name
        binding.tvPrice.text = rupiah(menu.price)
        binding.tvRating.text = rating(menu.rating)
        binding.tvSold.text = getString(
            R.string.sold_count, rupiah(menu.sold).removePrefix("Rp")
        )
        binding.tvDesc.text = menu.description
        binding.ivSpicy.visibility = if (menu.spicy) View.VISIBLE else View.GONE
        binding.tvQty.text = qty.toString()
        binding.tvTotalPrice.text = rupiah(menu.price * qty)

        if (warung != null) {
            binding.tvWarungName.text = warung.name
            binding.tvWarungAvatar.text = warung.name.firstOrNull()?.toString().orEmpty()
            binding.tvWarungAvatar.background.setTint(Color.parseColor(warung.colorHex))
        }

        // Explicit intent: buka profil warung (bawa objek Restaurant).
        binding.cardWarung.setOnClickListener {
            warung?.let {
                startActivity(
                    Intent(this, RestaurantDetailActivity::class.java).putExtra("restaurant", it)
                )
            }
        }
    }

    private fun setupActions() {
        binding.btnBack.setOnClickListener { finish() }

        // Implicit intent ACTION_SEND: bagikan menu ke aplikasi lain.
        binding.btnShare.setOnClickListener {
            val text = "Laper! Aku mau pesan ${menu.name} (${rupiah(menu.price)}) di " +
                "${DummyData.restaurantById(menu.restaurantId)?.name}. Gaskeun! 🔥"
            val share = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
                putExtra(Intent.EXTRA_TITLE, menu.name)
            }
            safeStart(Intent.createChooser(share, "Bagikan menu"), "Tidak ada aplikasi untuk membagikan")
        }

        // Implicit intent ACTION_VIEW geo:: buka lokasi warung di aplikasi peta.
        binding.btnLocation.setOnClickListener {
            val warung = DummyData.restaurantById(menu.restaurantId) ?: return@setOnClickListener
            val geo = Uri.parse("geo:${warung.lat},${warung.lng}?q=${warung.lat},${warung.lng}(${warung.name})")
            safeStart(Intent(Intent.ACTION_VIEW, geo), "Aplikasi peta tidak ditemukan")
        }

        binding.btnMinus.setOnClickListener {
            if (qty > 1) { qty--; renderQty() }
        }
        binding.btnPlus.setOnClickListener {
            if (qty < 20) { qty++; renderQty() }
        }

        binding.btnAddCart.setOnClickListener {
            CartManager.add(menu, qty, binding.etNote.text?.toString()?.trim().orEmpty())
            toast(getString(R.string.added_to_cart))
            startActivity(
                Intent(this, CartActivity::class.java)
                    .putExtra("justAdded", true)
            )
        }
    }

    private fun renderQty() {
        binding.tvQty.text = qty.toString()
        binding.tvTotalPrice.text = rupiah(menu.price * qty)
    }
}
