package com.example.laper

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.laper.data.CartManager
import com.example.laper.data.OrderRepository
import com.example.laper.data.SessionManager

/** Inisialisasi global: manager berbasis SharedPreferences + mode gelap tersimpan. */
class LaperApp : Application() {

    override fun onCreate() {
        super.onCreate()
        SessionManager.init(this)
        CartManager.init(this)
        OrderRepository.init(this)

        if (SessionManager.isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}
