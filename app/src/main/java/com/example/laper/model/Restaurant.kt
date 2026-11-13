package com.example.laper.model

import java.io.Serializable

/** Warung penjual makanan. */
data class Restaurant(
    val id: Int,
    val name: String,
    val description: String,
    val rating: Double,
    val lat: Double,
    val lng: Double,
    val phone: String,
    val openHours: String,
    val etaMinutes: Int,
    val colorHex: String
) : Serializable
