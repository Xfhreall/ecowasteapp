package com.example.ecowasteapp.model

data class UserPost(
    val id: String,
    val username: String, // Kita ganti email jadi username biar simpel
    val caption: String,
    val imageUri: Any?, // Bisa String (URL) atau Uri (Lokal)
    val timestamp: Long
)