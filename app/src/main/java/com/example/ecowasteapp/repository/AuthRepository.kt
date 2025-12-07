package com.example.ecowasteapp.repository

import android.content.Context
import com.example.ecowasteapp.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepository(context: Context) {
    private val auth = FirebaseAuth.getInstance()
    private val sessionManager = SessionManager(context)

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    /**
     * Login dengan Firebase dan simpan session
     */
    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user!!
            
            // Simpan session setelah login berhasil
            sessionManager.saveLoginSession(user.uid, user.email ?: email)
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Register dengan Firebase dan simpan session
     */
    suspend fun register(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user!!
            
            // Simpan session setelah register berhasil
            sessionManager.saveLoginSession(user.uid, user.email ?: email)
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Logout dari Firebase dan clear session
     */
    fun logout() {
        auth.signOut()
        sessionManager.clearSession()
    }

    /**
     * Cek apakah user masih dalam session yang valid
     * Return true jika Firebase user ada DAN session masih valid (< 6 jam)
     */
    fun isUserLoggedIn(): Boolean {
        return currentUser != null && sessionManager.isSessionValid()
    }

    /**
     * Cek dan restore session jika valid
     * Return true jika session valid dan Firebase user masih ada
     */
    fun restoreSession(): Boolean {
        val sessionValid = sessionManager.isSessionValid()
        val firebaseUserExists = currentUser != null
        
        // Jika session tidak valid tapi Firebase user masih ada, logout
        if (!sessionValid && firebaseUserExists) {
            logout()
            return false
        }
        
        // Jika Firebase user tidak ada tapi session masih ada, clear session
        if (!firebaseUserExists && sessionValid) {
            sessionManager.clearSession()
            return false
        }
        
        return sessionValid && firebaseUserExists
    }

    fun getCurrentUserId(): String? {
        return currentUser?.uid
    }

    fun getCurrentUserEmail(): String? {
        return currentUser?.email
    }
    
    /**
     * Get remaining session time in minutes
     */
    fun getRemainingSessionTime(): Long {
        return sessionManager.getRemainingSessionTime()
    }
}
