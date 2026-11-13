package com.example.laper.data

import android.content.Context
import com.example.laper.model.CartItem
import com.example.laper.model.Order
import com.example.laper.model.OrderStatus
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Riwayat pesanan, disimpan sebagai array JSON di SharedPreferences.
 * Item pesanan hanya menyimpan menuId lalu dipetakan ulang ke DummyData.
 */
object OrderRepository {

    private const val PREFS = "laper_orders"
    private const val KEY_ORDERS = "orders"

    /** Satu tahap status setiap 45 detik sejak pesanan dibuat (simulasi demo). */
    private const val STEP_MILLIS = 45_000L

    /** Data kurir (dummy). */
    val courierName = "Bang Ojak"
    val courierPhone = "+6281298765432"
    val courierVehicle = "Motor Vario Biru · D 1234 UV"

    private lateinit var prefs: android.content.SharedPreferences

    fun init(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }

    fun createOrder(
        items: List<CartItem>,
        subtotal: Int,
        deliveryFee: Int,
        discount: Int,
        total: Int,
        receiver: String,
        phone: String,
        address: String,
        payment: String
    ): Order {
        val code = "LP-" + (1000..9999).random()
        val order = Order(
            id = "ORD-" + System.currentTimeMillis(),
            code = code,
            items = ArrayList(items),
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            discount = discount,
            total = total,
            receiver = receiver,
            phone = phone,
            address = address,
            payment = payment,
            status = OrderStatus.RECEIVED,
            createdAt = System.currentTimeMillis()
        )
        save(order)
        return order
    }

    /** Status aktif dihitung dari selisih waktu (simulasi berjalan otomatis). */
    fun currentStatus(order: Order): OrderStatus {
        val elapsed = System.currentTimeMillis() - order.createdAt
        val step = (elapsed / STEP_MILLIS).toInt().coerceAtMost(OrderStatus.entries.size - 1)
        return OrderStatus.entries[step]
    }

    fun save(order: Order) {
        val arr = JSONArray(readRaw())
        arr.put(toJson(order))
        // Terbaru selalu paling depan.
        prefs.edit().putString(KEY_ORDERS, arr.toString()).apply()
    }

    /** Riwayat terbaru di urutan pertama. */
    fun history(): ArrayList<Order> {
        val result = ArrayList<Order>()
        runCatching {
            val arr = JSONArray(readRaw())
            for (i in 0 until arr.length()) {
                fromJson(arr.getJSONObject(i))?.let { result.add(it) }
            }
        }
        return result
    }

    fun formatTime(millis: Long): String =
        SimpleDateFormat("d MMM · HH.mm", Locale("id", "ID")).format(Date(millis))

    private fun readRaw(): String = prefs.getString(KEY_ORDERS, null) ?: "[]"

    // ---- JSON helpers ----

    private fun toJson(o: Order): JSONObject {
        val items = JSONArray()
        o.items.forEach {
            items.put(
                JSONObject()
                    .put("menuId", it.menuItem.id)
                    .put("qty", it.qty)
                    .put("note", it.note)
            )
        }
        return JSONObject()
            .put("id", o.id)
            .put("code", o.code)
            .put("items", items)
            .put("subtotal", o.subtotal)
            .put("deliveryFee", o.deliveryFee)
            .put("discount", o.discount)
            .put("total", o.total)
            .put("receiver", o.receiver)
            .put("phone", o.phone)
            .put("address", o.address)
            .put("payment", o.payment)
            .put("status", o.status.name)
            .put("createdAt", o.createdAt)
    }

    private fun fromJson(o: JSONObject): Order? {
        val items = ArrayList<CartItem>()
        val arr = o.optJSONArray("items") ?: return null
        for (i in 0 until arr.length()) {
            val it = arr.getJSONObject(i)
            val menu = DummyData.menuById(it.getInt("menuId")) ?: continue
            items.add(CartItem(menu, it.getInt("qty"), it.optString("note")))
        }
        val status = runCatching { OrderStatus.valueOf(o.optString("status")) }
            .getOrDefault(OrderStatus.DONE)
        return Order(
            id = o.optString("id"),
            code = o.optString("code"),
            items = items,
            subtotal = o.optInt("subtotal"),
            deliveryFee = o.optInt("deliveryFee"),
            discount = o.optInt("discount"),
            total = o.optInt("total"),
            receiver = o.optString("receiver"),
            phone = o.optString("phone"),
            address = o.optString("address"),
            payment = o.optString("payment"),
            status = status,
            createdAt = o.optLong("createdAt")
        )
    }
}
