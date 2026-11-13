package com.example.laper.model

/** Status pesanan, berurutan sesuai timeline. */
enum class OrderStatus(val label: String) {
    RECEIVED("Diterima Warung"),
    COOKING("Dimasak"),
    DELIVERING("Sedang Diantar"),
    DONE("Selesai");

    fun next(): OrderStatus? = entries.getOrNull(ordinal + 1)
}
