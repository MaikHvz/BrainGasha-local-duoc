package com.proyecto.braingasha.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val rarity: String, // "Common", "Rare", "Epic", "Legendary"
    val imageUri: String,
    val description: String,
    val power: Int,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_cards")
data class UserCard(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val cardId: Long,
    val quantity: Int = 1,
    val obtainedAt: Long = System.currentTimeMillis()
)
