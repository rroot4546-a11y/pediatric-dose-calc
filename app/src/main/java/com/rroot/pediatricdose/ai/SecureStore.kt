package com.rroot.pediatricdose.ai

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Encrypted on-device store for the OpenRouter API token + selected model.
 *
 * The token is wrapped with the device-bound master key (Android Keystore,
 * AES-256-GCM) so it never leaves the device in plaintext and is unreadable
 * to other apps even on a rooted device once a user passcode/biometric is
 * set. We deliberately do NOT log or transmit the token anywhere except to
 * api.openrouter.ai.
 */
class SecureStore(context: Context) {

    private val prefs: SharedPreferences = run {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    var openRouterToken: String?
        get() = prefs.getString(KEY_TOKEN, null)?.takeIf { it.isNotBlank() }
        set(value) {
            prefs.edit().apply {
                if (value.isNullOrBlank()) remove(KEY_TOKEN) else putString(KEY_TOKEN, value)
            }.apply()
        }

    var modelId: String
        get() = prefs.getString(KEY_MODEL, DEFAULT_MODEL) ?: DEFAULT_MODEL
        set(value) {
            prefs.edit().putString(KEY_MODEL, value).apply()
        }

    /** "system" / "light" / "dark". Defaults to system. */
    var themePref: String
        get() = prefs.getString(KEY_THEME, DEFAULT_THEME) ?: DEFAULT_THEME
        set(value) {
            prefs.edit().putString(KEY_THEME, value).apply()
        }

    companion object {
        private const val FILE_NAME = "pedicalc_secure"
        private const val KEY_TOKEN = "openrouter_token"
        private const val KEY_MODEL = "openrouter_model"
        private const val KEY_THEME = "theme_pref"
        const val DEFAULT_MODEL = "google/gemini-2.0-flash-001"
        const val DEFAULT_THEME = "system"
    }
}
