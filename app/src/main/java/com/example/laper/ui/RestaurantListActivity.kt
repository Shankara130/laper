package com.example.laper.ui

import android.os.Bundle
import com.example.laper.adapter.RestaurantAdapter
import com.example.laper.data.DummyData
import com.example.laper.databinding.ActivityRestaurantListBinding

/** Daftar 5 warung sekitar kampus (RecyclerView). */
class RestaurantListActivity : BaseActivity() {

    private lateinit var binding: ActivityRestaurantListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySystemBarInsets(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        binding.rvRestaurants.adapter = RestaurantAdapter(DummyData.restaurants)
    }
}
