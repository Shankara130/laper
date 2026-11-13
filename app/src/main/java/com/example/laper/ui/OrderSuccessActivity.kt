package com.example.laper.ui

import android.content.Intent
import android.os.Bundle
import com.example.laper.R
import com.example.laper.data.DummyData
import com.example.laper.data.OrderRepository
import com.example.laper.databinding.ActivityOrderSuccessBinding
import com.example.laper.model.Order
import com.example.laper.util.rupiah
import com.example.laper.util.safeStart

/**
 * Pesanan berhasil. Menerima objek Order (explicit + Serializable).
 * Implicit intent ACTION_SEND untuk membagikan ringkasan pesanan.
 */
class OrderSuccessActivity : BaseActivity() {

    private lateinit var binding: ActivityOrderSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySystemBarInsets(binding.root)

        @Suppress("DEPRECATION")
        val order = intent.getSerializableExtra("order") as? Order ?: return

        binding.tvOrderCode.text = order.code
        binding.tvItems.text = getString(
            R.string.items_count, order.items.sumOf { it.qty }
        ) + " · " + DummyData.restaurantById(order.items.first().menuItem.restaurantId)?.name
        binding.tvTotal.text = rupiah(order.total)
        binding.tvPayment.text = "${order.payment} · ${OrderRepository.formatTime(order.createdAt)}"

        binding.btnTrack.setOnClickListener {
            // Explicit intent + data: objek Order dibawa ke halaman lacak.
            startActivity(
                Intent(this, OrderTrackingActivity::class.java)
                    .putExtra("order", order)
                    .putExtra("isHistory", false)
            )
            finish()
        }
        binding.btnHome.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            )
            finish()
        }
        binding.btnShare.setOnClickListener {
            val text = "Pesanan Laper! berhasil dibuat 🎉\n" +
                "Kode: ${order.code}\n" +
                order.items.joinToString("\n") { "• ${it.menuItem.name} ×${it.qty}" } +
                "\nTotal: ${rupiah(order.total)} (${order.payment})"
            val share = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            safeStart(Intent.createChooser(share, "Bagikan pesanan"))
        }
    }
}
