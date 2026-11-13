package com.example.laper.ui

import android.os.Bundle
import android.view.View
import com.example.laper.adapter.OrderAdapter
import com.example.laper.data.OrderRepository
import com.example.laper.databinding.ActivityOrderHistoryBinding

/** Riwayat pesanan tersimpan (SharedPreferences JSON) di RecyclerView. */
class OrderHistoryActivity : BaseActivity() {

    private lateinit var binding: ActivityOrderHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySystemBarInsets(binding.root)

        binding.btnBack.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        val orders = OrderRepository.history()
        binding.rvOrders.adapter = OrderAdapter(orders)
        binding.layoutEmpty.visibility = if (orders.isEmpty()) View.VISIBLE else View.GONE
        binding.rvOrders.visibility = if (orders.isEmpty()) View.GONE else View.VISIBLE
    }
}
