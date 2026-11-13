package com.example.laper.model

/** Promo yang tampil di banner dashboard dan bisa dipakai di keranjang. */
data class Promo(
    val code: String,
    val title: String,
    val subtitle: String,
    val discountRp: Int,
    val minSpend: Int,
    val colorHex: String
)
