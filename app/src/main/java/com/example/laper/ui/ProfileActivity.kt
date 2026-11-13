package com.example.laper.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.laper.data.SessionManager
import com.example.laper.databinding.ActivityProfileBinding

/**
 * Profil pengguna + pengaturan: mode gelap (tersimpan di SharedPreferences),
 * riwayat pesanan, tentang aplikasi, dan keluar.
 */
class ProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySystemBarInsets(binding.root)

        val user = SessionManager.currentUser ?: SessionManager.demoUser
        binding.tvAvatar.text = user.name.firstOrNull()?.toString().orEmpty()
        binding.tvAvatar.background.setTint(Color.parseColor("#C2410C"))
        binding.tvName.text = user.name
        binding.tvEmail.text = user.email
        binding.tvPhone.text = "62${user.phone.removePrefix("62")} · Mahasiswa"

        binding.btnBack.setOnClickListener { finish() }
        binding.rowHistory.setOnClickListener {
            startActivity(Intent(this, OrderHistoryActivity::class.java))
        }
        binding.rowAbout.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        binding.switchDark.isChecked = SessionManager.isDarkMode
        binding.switchDark.setOnCheckedChangeListener { _, checked ->
            SessionManager.setDarkMode(checked)
            AppCompatDelegate.setDefaultNightMode(
                if (checked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        binding.btnLogout.setOnClickListener {
            SessionManager.logout()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
