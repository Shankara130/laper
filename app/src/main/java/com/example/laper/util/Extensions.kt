package com.example.laper.util

import android.content.Context
import android.view.View
import android.widget.Toast
import android.content.Intent

/** Toast pendek. */
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

/**
 * Jalankan implicit intent dengan aman: jika tidak ada aplikasi yang bisa
 * menangani, tampilkan pesan daripada crash.
 */
fun Context.safeStart(intent: Intent, fallbackMessage: String = "Tidak ada aplikasi yang bisa membuka ini") {
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        toast(fallbackMessage)
    }
}
