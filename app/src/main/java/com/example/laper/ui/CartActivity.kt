package com.example.laper.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.laper.R
import com.example.laper.adapter.CartAdapter
import com.example.laper.data.CartManager
import com.example.laper.data.DummyData
import com.example.laper.databinding.ActivityCartBinding
import com.example.laper.util.rupiah
import com.example.laper.util.toast
import java.io.Serializable

/**
 * Keranjang: ubah jumlah, hapus, kode promo, ringkasan biaya.
 * Lanjut ke CheckoutActivity membawa ArrayList&lt;CartItem&gt; (explicit intent + data).
 */
class CartActivity : BaseActivity() {

    private lateinit var binding: ActivityCartBinding
    private var discount = 0
    private var promoCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySystemBarInsets(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        binding.btnBrowse.setOnClickListener {
            startActivity(Intent(this, MenuListActivity::class.java))
        }
        binding.btnApplyPromo.setOnClickListener { applyPromo() }
        binding.btnCheckout.setOnClickListener { goToCheckout() }
        render()
    }

    private fun render() {
        val items = CartManager.list
        val hasItems = items.isNotEmpty()

        binding.rvCart.visibility = if (hasItems) View.VISIBLE else View.GONE
        binding.cardSummary.visibility = if (hasItems) View.VISIBLE else View.GONE
        binding.layoutEmpty.visibility = if (hasItems) View.GONE else View.VISIBLE
        binding.tvItemCount.text = getString(R.string.items_count, CartManager.itemCount)

        if (!hasItems) return

        binding.rvCart.adapter = CartAdapter(
            items,
            onQtyChanged = { index, qty -> CartManager.setQty(index, qty); render() },
            onRemoved = { index -> CartManager.removeAt(index); render() }
        )

        val subtotal = CartManager.subtotal
        val delivery = CartManager.deliveryFee(subtotal)
        // Promo bisa jadi tak valid lagi setelah item diubah — validasi ulang.
        validateCurrentPromo(subtotal)

        binding.tvSubtotal.text = rupiah(subtotal)
        binding.tvDelivery.text = if (delivery == 0) "Gratis" else rupiah(delivery)
        binding.layoutDiscount.visibility = if (discount > 0) View.VISIBLE else View.GONE
        binding.tvDiscount.text = "-${rupiah(discount)}"
        binding.tvTotal.text = rupiah(subtotal + delivery - discount)
    }

    private fun applyPromo() {
        val code = binding.etPromo.text?.toString()?.trim().orEmpty()
        if (code.isEmpty()) return

        val promo = DummyData.promoByCode(code)
        if (promo == null || CartManager.subtotal < promo.minSpend) {
            discount = 0
            promoCode = null
            toast(getString(R.string.promo_invalid))
        } else {
            discount = promo.discountRp
            promoCode = promo.code
            toast(getString(R.string.promo_applied, promo.code))
        }
        render()
    }

    private fun validateCurrentPromo(subtotal: Int) {
        val code = promoCode ?: return
        val promo = DummyData.promoByCode(code)
        if (promo == null || subtotal < promo.minSpend) {
            discount = 0
            promoCode = null
        } else {
            discount = promo.discountRp
        }
    }

    private fun goToCheckout() {
        val items = ArrayList(CartManager.list)
        val subtotal = CartManager.subtotal
        val delivery = CartManager.deliveryFee(subtotal)
        val intent = Intent(this, CheckoutActivity::class.java)
            .putExtra("items", items as Serializable)
            .putExtra("subtotal", subtotal)
            .putExtra("delivery", delivery)
            .putExtra("discount", discount)
            .putExtra("promoCode", promoCode ?: "")
        startActivity(intent)
    }
}
