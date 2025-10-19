package com.proyecto.braingasha.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val password: String,
    val username: String,
    val profileImageUri: String? = null,
    val coins: Int = 1000,
    val createdAt: Long = System.currentTimeMillis()
)
