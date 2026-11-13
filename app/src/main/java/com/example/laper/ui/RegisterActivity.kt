package com.example.laper.ui

import android.content.Intent
import android.os.Bundle
import com.example.laper.R
import com.example.laper.data.SessionManager
import com.example.laper.databinding.ActivityRegisterBinding
import com.example.laper.model.User
import com.example.laper.util.toast

/** Registrasi akun baru (disimpan di SharedPreferences sebagai akun aktif). */
class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySystemBarInsets(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        binding.btnGoLogin.setOnClickListener { finish() }
        binding.btnRegister.setOnClickListener { attemptRegister() }
    }

    private fun attemptRegister() {
        val name = binding.etName.text?.toString()?.trim().orEmpty()
        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
        val phone = binding.etPhone.text?.toString()?.trim().orEmpty()
        val password = binding.etPassword.text?.toString().orEmpty()
        val confirm = binding.etConfirm.text?.toString().orEmpty()

        var valid = true
        if (name.isEmpty()) { binding.tilName.error = getString(R.string.err_name_required); valid = false } else binding.tilName.error = null
        if (email.isEmpty()) { binding.tilEmail.error = getString(R.string.err_email_required); valid = false }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.err_email_invalid); valid = false
        } else binding.tilEmail.error = null
        if (phone.isEmpty()) { binding.tilPhone.error = getString(R.string.err_phone_required); valid = false } else binding.tilPhone.error = null
        if (password.length < 6) { binding.tilPassword.error = getString(R.string.err_password_short); valid = false } else binding.tilPassword.error = null
        if (confirm != password) { binding.tilConfirm.error = getString(R.string.err_password_mismatch); valid = false } else binding.tilConfirm.error = null
        if (!valid) return

        SessionManager.register(User(name, email, "62$phone", password))
        toast(getString(R.string.register_success))

        // Kembali ke login membawa email (explicit intent + data).
        val intent = Intent(this, LoginActivity::class.java).putExtra("prefilledEmail", email)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}
