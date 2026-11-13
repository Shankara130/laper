package com.example.laper.ui

import android.content.Intent
import android.os.Bundle
import com.example.laper.R
import com.example.laper.data.SessionManager
import com.example.laper.databinding.ActivityLoginBinding
import com.example.laper.util.toast

/**
 * Login dummy: akun demo demo@student.ac.id / bp3123,
 * atau akun yang terdaftar lewat RegisterActivity.
 */
class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySystemBarInsets(binding.root)

        // Email terisi otomatis setelah register (explicit intent + data).
        intent.getStringExtra("prefilledEmail")?.let {
            binding.etEmail.setText(it)
        }

        binding.btnLogin.setOnClickListener { attemptLogin() }
        binding.btnGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun attemptLogin() {
        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
        val password = binding.etPassword.text?.toString().orEmpty()

        var valid = true
        if (email.isEmpty()) {
            binding.tilEmail.error = getString(R.string.err_email_required); valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.err_email_invalid); valid = false
        } else binding.tilEmail.error = null

        if (password.isEmpty()) {
            binding.tilPassword.error = getString(R.string.err_password_required); valid = false
        } else binding.tilPassword.error = null

        if (!valid) return

        val user = SessionManager.login(email, password)
        if (user != null) {
            toast("Selamat datang, ${user.name.split(" ").first()}!")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            toast(getString(R.string.login_failed))
        }
    }
}
