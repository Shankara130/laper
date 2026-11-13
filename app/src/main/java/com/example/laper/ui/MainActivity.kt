package com.example.laper.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.laper.R
import com.example.laper.adapter.PopularAdapter
import com.example.laper.adapter.PromoAdapter
import com.example.laper.adapter.RestaurantAdapter
import com.example.laper.data.CartManager
import com.example.laper.data.DummyData
import com.example.laper.data.SessionManager
import com.example.laper.databinding.ActivityMainBinding
import java.util.Calendar

/**
 * Dashboard: banner promo (ViewPager2 auto-next), chips kategori,
 * menu terlaris (RV horizontal), daftar warung (RV), bottom navigation.
 */
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private val bannerRunnable = object : Runnable {
        override fun run() {
            val count = binding.vpPromo.adapter?.itemCount ?: 0
            if (count > 0) {
                binding.vpPromo.currentItem = (binding.vpPromo.currentItem + 1) % count
            }
            handler.postDelayed(this, 3500)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySystemBarInsets(binding.root)

        setupHeader()
        setupBanner()
        setupChips()
        setupLists()
        setupBottomNav()
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(bannerRunnable, 3500)
        binding.bottomNav.selectedItemId = R.id.nav_home
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(bannerRunnable)
    }

    private fun setupHeader() {
        val user = SessionManager.currentUser ?: SessionManager.demoUser
        binding.tvGreeting.text = greeting()
        binding.tvUserName.text = user.name.split(" ").take(2).joinToString(" ")
        binding.tvAvatar.text = user.name.firstOrNull()?.toString().orEmpty()
        binding.tvAvatar.background.setTint(Color.parseColor("#C2410C"))

        binding.tvAvatar.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        binding.btnCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        binding.cardSearch.setOnClickListener {
            startActivity(Intent(this, MenuListActivity::class.java))
        }
        binding.btnSeeAllMenu.setOnClickListener {
            startActivity(Intent(this, MenuListActivity::class.java))
        }
    }

    private fun setupBanner() {
        binding.vpPromo.adapter = PromoAdapter()
        binding.vpPromo.registerOnPageChangeCallback(object :
            androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateDots(position)
            }
        })
        buildDots()
    }

    private fun buildDots() {
        binding.layoutDots.removeAllViews()
        val inflater = LayoutInflater.from(this)
        repeat(DummyData.promos.size) { index ->
            val dot = inflater.inflate(R.layout.item_dot, binding.layoutDots, false) as ImageView
            binding.layoutDots.addView(dot)
        }
        updateDots(0)
    }

    private fun updateDots(active: Int) {
        for (i in 0 until binding.layoutDots.childCount) {
            val dot = binding.layoutDots.getChildAt(i) as ImageView
            val size = if (i == active) 18 else 8
            val lp = dot.layoutParams as LinearLayout.LayoutParams
            lp.width = size * 3
            dot.layoutParams = lp
            dot.setImageResource(if (i == active) R.drawable.bg_dot_active else R.drawable.bg_dot_inactive)
        }
    }

    private fun setupChips() {
        binding.layoutChips.removeAllViews()
        DummyData.categories.forEach { category ->
            val chip = com.google.android.material.chip.Chip(this)
            chip.text = category
            chip.isCheckable = false
            chip.chipBackgroundColor =
                android.content.res.ColorStateList.valueOf(
                    getColor(com.example.laper.R.color.primary_container)
                )
            chip.chipStrokeWidth = 0f
            chip.setTextColor(getColor(com.example.laper.R.color.on_primary_container))
            chip.setOnClickListener {
                // Explicit intent + data kategori terpilih.
                startActivity(
                    Intent(this, MenuListActivity::class.java).putExtra("category", category)
                )
            }
            binding.layoutChips.addView(chip)
        }
    }

    private fun setupLists() {
        binding.rvPopular.adapter = PopularAdapter(DummyData.mostSold())
        binding.rvRestaurants.adapter = RestaurantAdapter(DummyData.restaurants)
    }

    private fun setupBottomNav() {
        binding.bottomNav.selectedItemId = R.id.nav_home
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_cart -> {
                    startActivity(Intent(this, CartActivity::class.java)); false
                }
                R.id.nav_history -> {
                    startActivity(Intent(this, OrderHistoryActivity::class.java)); false
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java)); false
                }
                else -> false
            }
        }
    }

    private fun greeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 4..10 -> getString(R.string.greeting_morning)
            in 11..14 -> getString(R.string.greeting_afternoon)
            in 15..17 -> getString(R.string.greeting_sore)
            else -> getString(R.string.greeting_evening)
        }
    }
}
