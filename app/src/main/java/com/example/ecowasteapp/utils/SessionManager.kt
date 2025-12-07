package com.example.ecowasteapp.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * SessionManager untuk mengelola persistent login session.
 * Session berlaku selama 6 jam (21600000 ms) sejak login terakhir.
 */
class SessionManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREFS_NAME = "eco_waste_session"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_LOGIN_TIMESTAMP = "login_timestamp"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_ID = "user_id"
        
        // Session duration: 6 hours in milliseconds
        private const val SESSION_DURATION = 6 * 60 * 60 * 1000L // 6 jam = 21600000 ms
    }
    
    /**
     * Simpan data login session
     */
    fun saveLoginSession(userId: String, email: String) {
        val editor = prefs.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putLong(KEY_LOGIN_TIMESTAMP, System.currentTimeMillis())
        editor.putString(KEY_USER_EMAIL, email)
        editor.putString(KEY_USER_ID, userId)
        editor.apply()
    }
    
    /**
     * Cek apakah user masih dalam session yang valid (< 6 jam)
     */
    fun isSessionValid(): Boolean {
        val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        if (!isLoggedIn) return false
        
        val loginTimestamp = prefs.getLong(KEY_LOGIN_TIMESTAMP, 0L)
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - loginTimestamp
        
        // Jika lebih dari 6 jam, session tidak valid
        return elapsedTime < SESSION_DURATION
    }
    
    /**
     * Ambil user ID dari session
     */
    fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }
    
    /**
     * Ambil user email dari session
     */
    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }
    
    /**
     * Clear semua data session (logout)
     */
    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
    
    /**
     * Update login timestamp (refresh session)
     */
    fun refreshSession() {
        if (prefs.getBoolean(KEY_IS_LOGGED_IN, false)) {
            val editor = prefs.edit()
            editor.putLong(KEY_LOGIN_TIMESTAMP, System.currentTimeMillis())
            editor.apply()
        }
    }
    
    /**
     * Ambil sisa waktu session dalam menit
     */
    fun getRemainingSessionTime(): Long {
        if (!isSessionValid()) return 0L
        
        val loginTimestamp = prefs.getLong(KEY_LOGIN_TIMESTAMP, 0L)
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - loginTimestamp
        val remainingTime = SESSION_DURATION - elapsedTime
        
        // Convert ke menit
        return remainingTime / (60 * 1000)
    }
}
