package com.example.laper.data

import android.content.Context
import com.example.laper.model.User
import org.json.JSONObject

/**
 * Sesi login & pengaturan pengguna, disimpan di SharedPreferences.
 * Akun demo: demo@student.ac.id / bp3123 (selalu tersedia).
 */
object SessionManager {

    private const val PREFS = "laper_session"
    private const val KEY_LOGGED_IN = "logged_in"
    private const val KEY_USER = "user"
    private const val KEY_REGISTERED = "registered_user"
    private const val KEY_DARK_MODE = "dark_mode"

    val demoUser = User(
        name = "Dimas Pratama",
        email = "demo@student.ac.id",
        phone = "081234567890",
        password = "bp3123"
    )

    private lateinit var prefs: android.content.SharedPreferences

    fun init(context: Context) {
        prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }

    // ---- Login / register ----

    fun login(email: String, password: String): User? {
        val registered = registeredUser()
        val match = when {
            email.equals(demoUser.email, ignoreCase = true) && password == demoUser.password -> demoUser
            registered != null &&
                email.equals(registered.email, ignoreCase = true) &&
                password == registered.password -> registered
            else -> null
        }
        if (match != null) {
            prefs.edit()
                .putBoolean(KEY_LOGGED_IN, true)
                .putString(KEY_USER, userToJson(match).toString())
                .apply()
        }
        return match
    }

    fun register(user: User) {
        prefs.edit().putString(KEY_REGISTERED, userToJson(user).toString()).apply()
    }

    fun registeredUser(): User? =
        prefs.getString(KEY_REGISTERED, null)?.let { userFromJson(JSONObject(it)) }

    fun logout() {
        prefs.edit()
            .putBoolean(KEY_LOGGED_IN, false)
            .remove(KEY_USER)
            .apply()
    }

    val isLoggedIn: Boolean get() = prefs.getBoolean(KEY_LOGGED_IN, false)

    val currentUser: User?
        get() = prefs.getString(KEY_USER, null)?.let { userFromJson(JSONObject(it)) }
            ?: if (isLoggedIn) demoUser else null

    // ---- Pengaturan ----

    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }

    val isDarkMode: Boolean get() = prefs.getBoolean(KEY_DARK_MODE, false)

    // ---- JSON helpers ----

    private fun userToJson(u: User) = JSONObject()
        .put("name", u.name)
        .put("email", u.email)
        .put("phone", u.phone)
        .put("password", u.password)

    private fun userFromJson(o: JSONObject): User = User(
        name = o.optString("name"),
        email = o.optString("email"),
        phone = o.optString("phone"),
        password = o.optString("password")
    )
}
