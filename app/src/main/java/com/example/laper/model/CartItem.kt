package com.example.laper.model

import java.io.Serializable

/** MenuItem + jumlah + catatan di dalam keranjang. */
data class CartItem(
    val menuItem: MenuItem,
    val qty: Int,
    val note: String = ""
) : Serializable {
    val lineTotal: Int get() = menuItem.price * qty
}
