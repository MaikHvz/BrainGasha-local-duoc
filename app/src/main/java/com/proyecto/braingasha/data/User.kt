package com.proyecto.braingasha.data

data class User(
    val id: Long = 0,
    val email: String,
    val password: String? = null,
    val username: String,
    val profileImageUri: String? = null,
    val coins: Int = 1000,
    val totalCartas: Int = 0,
    val totalTiradas: Int = 0
)
