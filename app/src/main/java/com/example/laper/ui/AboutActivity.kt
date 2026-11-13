package com.example.laper.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.example.laper.BuildConfig
import com.example.laper.R
import com.example.laper.databinding.ActivityAboutBinding
import com.example.laper.util.safeStart

/**
 * Tentang aplikasi. Implicit intents: ACTION_SENDTO mailto (email developer)
 * dan ACTION_VIEW market:// (beri ulasan) dengan fallback http.
 */
class AboutActivity : BaseActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySystemBarInsets(binding.root)

        binding.tvVersion.text = getString(R.string.version_label, BuildConfig.VERSION_NAME)
        binding.btnBack.setOnClickListener { finish() }

        // Implicit intent ACTION_SENDTO mailto: dengan subject & body.
        binding.btnEmail.setOnClickListener {
            val mailto = Uri.parse("mailto:dev@laper.app").buildUpon()
                .appendQueryParameter("subject", "Feedback Laper!")
                .appendQueryParameter("body", "Halo tim Laper! Aku punya masukan…")
                .build()
            safeStart(
                Intent(Intent.ACTION_SENDTO, mailto),
                "Aplikasi email tidak ditemukan"
            )
        }

        // Implicit intent ACTION_VIEW market:// dengan fallback http.
        binding.btnRate.setOnClickListener {
            val market = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
            )
            if (market.resolveActivity(packageManager) != null) {
                startActivity(market)
            } else {
                safeStart(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
                    ),
                    "Browser tidak ditemukan"
                )
            }
        }
    }
}
