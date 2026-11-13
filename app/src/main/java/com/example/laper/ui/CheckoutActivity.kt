package com.example.laper.ui

import android.content.Intent
import android.os.Bundle
import com.example.laper.R
import com.example.laper.data.CartManager
import com.example.laper.data.OrderRepository
import com.example.laper.data.SessionManager
import com.example.laper.databinding.ActivityCheckoutBinding
import com.example.laper.model.CartItem
import com.example.laper.util.rupiah
import com.example.laper.util.toast
import java.io.Serializable

/**
 * Checkout: form pengiriman + metode pembayaran + ringkasan.
 * Menerima ArrayList<CartItem> dari CartActivity (explicit intent + data),
 * lalu membuat Order dan mengirimkannya ke OrderSuccessActivity.
 */
class CheckoutActivity : BaseActivity() {

    private lateinit var binding: ActivityCheckoutBinding

    private val items = ArrayList<CartItem>()
    private var subtotal = 0
    private var delivery = 0
    private var discount = 0
    private var promoCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySystemBarInsets(binding.root)

        @Suppress("DEPRECATION")
        (intent.getSerializableExtra("items") as? ArrayList<CartItem>)?.let { items.addAll(it) }
        subtotal = intent.getIntExtra("subtotal", 0)
        delivery = intent.getIntExtra("delivery", 0)
        discount = intent.getIntExtra("discount", 0)
        promoCode = intent.getStringExtra("promoCode").orEmpty()

        // Prefill dari profil pengguna (kenyamanan UI/UX).
        SessionManager.currentUser?.let { user ->
            if (binding.etReceiver.text.isNullOrBlank()) {
                binding.etReceiver.setText(user.name)
            }
            if (binding.etPhone.text.isNullOrBlank() && user.phone.isNotBlank()) {
                binding.etPhone.setText(user.phone.removePrefix("62"))
            }
        }

        binding.btnBack.setOnClickListener { finish() }
        binding.btnOrder.setOnClickListener { placeOrder() }
        renderSummary()
    }

    private fun renderSummary() {
        val listText = items.joinToString("\n") {
            "• ${it.menuItem.name} ×${it.qty}"
        }
        binding.tvItems.text = listText
        binding.tvSubtotal.text = rupiah(subtotal)
        binding.tvDelivery.text = if (delivery == 0) "Gratis" else rupiah(delivery)
        binding.layoutDiscount.visibility = if (discount > 0) android.view.View.VISIBLE else android.view.View.GONE
        binding.tvDiscount.text = "-${rupiah(discount)}"
        val total = subtotal + delivery - discount
        binding.tvTotal.text = rupiah(total)
        binding.tvTotalBar.text = rupiah(total)
    }

    private fun paymentLabel(): String = when (binding.rgPayment.checkedRadioButtonId) {
        R.id.rb_qris -> getString(R.string.pay_qris)
        R.id.rb_dana -> getString(R.string.pay_dana)
        R.id.rb_cod -> getString(R.string.pay_cod)
        else -> getString(R.string.pay_transfer)
    }

    private fun placeOrder() {
        val receiver = binding.etReceiver.text?.toString()?.trim().orEmpty()
        val phone = binding.etPhone.text?.toString()?.trim().orEmpty()
        val address = binding.etAddress.text?.toString()?.trim().orEmpty()

        var valid = true
        if (receiver.isEmpty()) { binding.tilReceiver.error = getString(R.string.err_receiver_required); valid = false } else binding.tilReceiver.error = null
        if (phone.isEmpty()) { binding.tilPhone.error = getString(R.string.err_phone_required); valid = false } else binding.tilPhone.error = null
        if (address.isEmpty()) { binding.tilAddress.error = getString(R.string.err_address_required); valid = false } else binding.tilAddress.error = null
        if (!valid) return

        val order = OrderRepository.createOrder(
            items = items,
            subtotal = subtotal,
            deliveryFee = delivery,
            discount = discount,
            total = subtotal + delivery - discount,
            receiver = receiver,
            phone = "62$phone",
            address = address,
            payment = paymentLabel()
        )
        CartManager.clear()

        // Explicit intent + data: objek Order (Serializable).
        val intent = Intent(this, OrderSuccessActivity::class.java).putExtra("order", order)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}
