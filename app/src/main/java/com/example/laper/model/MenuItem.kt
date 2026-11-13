package com.example.laper.model

import java.io.Serializable

/** Satu item menu warung. Dikirim antar Activity lewat Intent (Serializable). */
data class MenuItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val category: String,
    val imageRes: Int,
    val restaurantId: Int,
    val rating: Double,
    val spicy: Boolean = false,
    val sold: Int = 0
) : Serializable
