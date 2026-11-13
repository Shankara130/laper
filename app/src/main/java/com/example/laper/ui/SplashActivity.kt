package com.example.laper.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.laper.data.SessionManager

/**
 * Splash screen (core-splashscreen), lalu routing:
 * sudah login -> MainActivity, belum -> LoginActivity.
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            val target = if (SessionManager.isLoggedIn) MainActivity::class.java else LoginActivity::class.java
            startActivity(Intent(this, target).putExtra("isReturningUser", SessionManager.isLoggedIn))
            finish()
        }, 900)
    }
}
