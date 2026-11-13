package com.example.laper.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.laper.R
import com.example.laper.data.CartManager
import com.example.laper.data.OrderRepository
import com.example.laper.databinding.ActivityOrderTrackingBinding
import com.example.laper.model.Order
import com.example.laper.model.OrderStatus
import com.example.laper.util.rupiah
import com.example.laper.util.safeStart
import com.example.laper.util.toast

/**
 * Lacak pesanan dengan timeline status (Diterima -> Dimasak -> Diantar -> Selesai).
 * Status dihitung dari waktu pemesanan (simulasi berjalan otomatis).
 * Implicit intent ACTION_DIAL untuk menelepon kurir.
 */
class OrderTrackingActivity : BaseActivity() {

    private lateinit var binding: ActivityOrderTrackingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySystemBarInsets(binding.root)

        @Suppress("DEPRECATION")
        val order = intent.getSerializableExtra("order") as? Order ?: return
        val isHistory = intent.getBooleanExtra("isHistory", false)
        val current = OrderRepository.currentStatus(order)

        binding.tvOrderCode.text = order.code
        binding.tvStatus.text = "${current.label} · ${OrderRepository.formatTime(order.createdAt)}"

        binding.tvCourierAvatar.text = OrderRepository.courierName.firstOrNull()?.toString().orEmpty()
        binding.tvCourierAvatar.background.setTint(Color.parseColor("#3D6B35"))
        binding.tvCourierName.text = OrderRepository.courierName
        binding.tvCourierVehicle.text = OrderRepository.courierVehicle

        binding.tvItems.text = order.items.joinToString("\n") {
            "• ${it.menuItem.name} ×${it.qty}"
        }
        binding.tvTotal.text = rupiah(order.total)
        binding.tvAddress.text = "${order.receiver} · ${order.address}"

        // Implicit intent ACTION_DIAL: telepon kurir.
        binding.btnCallCourier.setOnClickListener {
            safeStart(
                Intent(Intent.ACTION_DIAL, Uri.parse("tel:${OrderRepository.courierPhone}")),
                "Aplikasi telepon tidak ditemukan"
            )
        }

        buildTimeline(order, current)

        if (isHistory) {
            binding.btnReorder.visibility = View.VISIBLE
            binding.btnReorder.setOnClickListener {
                order.items.forEach { CartManager.add(it.menuItem, it.qty, it.note) }
                toast("Item ditambahkan ke keranjang")
                startActivity(Intent(this, CartActivity::class.java))
            }
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    private fun buildTimeline(order: Order, current: OrderStatus) {
        val container = binding.layoutSteps
        container.removeAllViews()

        OrderStatus.entries.forEachIndexed { index, status ->
            val reached = status.ordinal <= current.ordinal
            val active = status.ordinal == current.ordinal

            val row = layoutInflater.inflate(R.layout.item_step, container, false)
            val circle = row.findViewById<LinearLayout>(R.id.step_circle)
            row.findViewById<ImageView>(R.id.step_icon).visibility =
                if (reached) View.VISIBLE else View.INVISIBLE
            circle.setBackgroundResource(
                if (reached) R.drawable.bg_step_done else R.drawable.bg_step_upcoming
            )
            val label = row.findViewById<TextView>(R.id.step_label)
            label.text = status.label
            label.setTextColor(
                getColor(if (reached) com.example.laper.R.color.on_surface else com.example.laper.R.color.on_surface_variant)
            )
            label.setTypeface(null, if (active) android.graphics.Typeface.BOLD else android.graphics.Typeface.NORMAL)

            if (reached) {
                val time = row.findViewById<TextView>(R.id.step_time)
                time.visibility = View.VISIBLE
                time.text = OrderRepository.formatTime(order.createdAt + index * 45_000L)
            }
            container.addView(row)

            if (index < OrderStatus.entries.size - 1) {
                val connector = View(this)
                connector.layoutParams = LinearLayout.LayoutParams(4, 28).apply {
                    marginStart = 12
                }
                connector.setBackgroundColor(
                    getColor(
                        if (status.ordinal < current.ordinal)
                            com.example.laper.R.color.primary
                        else com.example.laper.R.color.outline_variant
                    )
                )
                container.addView(connector)
            }
        }
    }
}
