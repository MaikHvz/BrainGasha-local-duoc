package com.proyecto.braingasha.data

data class User(
    val id: Long = 0,
    val email: String,
    val password: String,
    val username: String,
    val profileImageUri: String? = null,
    val coins: Int = 1000
)
