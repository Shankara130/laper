package com.example.laper.util

import java.text.NumberFormat
import java.util.Locale

/** Format angka ke Rupiah: 18000 -> "Rp18.000". */
fun rupiah(value: Int): String =
    "Rp" + NumberFormat.getIntegerInstance(Locale("id", "ID")).format(value)

/** Rating dengan satu desimal: 4.85 -> "4.9". */
fun rating(value: Double): String = String.format(Locale.US, "%.1f", value)
