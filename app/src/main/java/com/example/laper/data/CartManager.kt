package com.example.laper.data

import android.content.Context
import com.example.laper.model.CartItem
import com.example.laper.model.MenuItem
import org.json.JSONArray
import org.json.JSONObject

/**
 * Keranjang belanja global (singleton) yang bertahan antar Activity
 * dan disimpan ke SharedPreferences agar aman dari process death.
 */
object CartManager {

    private const val PREFS = "laper_cart"
    private const val KEY_ITEMS = "items"

    /** Ongkir gratis untuk subtotal minimal [FREE_DELIVERY_MIN]. */
    const val DELIVERY_FEE = 6000
    const val FREE_DELIVERY_MIN = 50000

    private lateinit var prefs: android.content.SharedPreferences
    private val items = ArrayList<CartItem>()

    /** Callback dipanggil setiap isi keranjang berubah (untuk badge, dll). */
    var onChanged: (() -> Unit)? = null

    fun init(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        load()
    }

    val list: List<CartItem> get() = items

    val itemCount: Int get() = items.sumOf { it.qty }

    val subtotal: Int get() = items.sumOf { it.lineTotal }

    fun deliveryFee(subtotal: Int = this.subtotal): Int =
        if (subtotal >= FREE_DELIVERY_MIN || subtotal == 0) 0 else DELIVERY_FEE

    fun add(menu: MenuItem, qty: Int, note: String) {
        val existing = items.indexOfFirst { it.menuItem.id == menu.id && it.note == note }
        if (existing >= 0) {
            val cur = items[existing]
            items[existing] = cur.copy(qty = cur.qty + qty)
        } else {
            items.add(CartItem(menu, qty, note))
        }
        persistAndNotify()
    }

    fun setQty(index: Int, qty: Int) {
        when {
            qty <= 0 -> items.removeAt(index)
            else -> items[index] = items[index].copy(qty = qty)
        }
        persistAndNotify()
    }

    fun removeAt(index: Int) {
        items.removeAt(index)
        persistAndNotify()
    }

    fun clear() {
        items.clear()
        persistAndNotify()
    }

    private fun persistAndNotify() {
        persist()
        onChanged?.invoke()
    }

    // ---- Persistensi JSON ----

    private fun persist() {
        val arr = JSONArray()
        items.forEach { item ->
            arr.put(
                JSONObject()
                    .put("menuId", item.menuItem.id)
                    .put("qty", item.qty)
                    .put("note", item.note)
            )
        }
        prefs.edit().putString(KEY_ITEMS, arr.toString()).apply()
    }

    private fun load() {
        items.clear()
        val raw = prefs.getString(KEY_ITEMS, null) ?: return
        runCatching {
            val arr = JSONArray(raw)
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                val menu = DummyData.menuById(o.getInt("menuId")) ?: continue
                items.add(CartItem(menu, o.getInt("qty"), o.optString("note")))
            }
        }
    }
}
