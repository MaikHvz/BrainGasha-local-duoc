package com.proyecto.braingasha.data.remote.dto

data class UserResponse(
    val id: Long?,
    val email: String,
    val username: String,
    val profileImageUri: String?,
    val coins: Int?,
    val totalCartas: Int?,
    val totalTiradas: Int?
)