package com.example.ecowasteapp.model

data class UserPost(
    val id: String = "",
    val userId: String = "", // ID user yang membuat post
    val username: String = "", // Username atau email
    val caption: String = "",
    val imageUrl: String? = null, // URL gambar di Firebase Storage
    val timestamp: Long = System.currentTimeMillis()
)