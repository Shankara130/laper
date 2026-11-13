package com.example.laper.model

import java.io.Serializable

/** Pesanan final hasil checkout. */
data class Order(
    val id: String,
    val code: String,
    val items: ArrayList<CartItem>,
    val subtotal: Int,
    val deliveryFee: Int,
    val discount: Int,
    val total: Int,
    val receiver: String,
    val phone: String,
    val address: String,
    val payment: String,
    val status: OrderStatus = OrderStatus.RECEIVED,
    val createdAt: Long = 0L
) : Serializable
