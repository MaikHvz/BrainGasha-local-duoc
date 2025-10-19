package com.proyecto.braingasha.data.dao

import androidx.room.*
import com.proyecto.braingasha.data.entity.Card
import com.proyecto.braingasha.data.entity.UserCard
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM cards")
    suspend fun getAllCards(): List<Card>

    @Query("SELECT * FROM cards WHERE id = :cardId")
    suspend fun getCardById(cardId: Long): Card?

    @Query("SELECT * FROM cards WHERE rarity = :rarity")
    suspend fun getCardsByRarity(rarity: String): List<Card>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: Card): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserCard(userCard: UserCard)

    @Query("SELECT c.*, uc.quantity FROM cards c " +
            "INNER JOIN user_cards uc ON c.id = uc.cardId " +
            "WHERE uc.userId = :userId")
    suspend fun getUserCards(userId: Long): List<CardWithQuantity>

    @Query("SELECT c.*, uc.quantity FROM cards c " +
            "INNER JOIN user_cards uc ON c.id = uc.cardId " +
            "WHERE uc.userId = :userId")
    fun getUserCardsFlow(userId: Long): Flow<List<CardWithQuantity>>

    @Query("SELECT COUNT(*) FROM user_cards WHERE userId = :userId")
    suspend fun getUserCardCount(userId: Long): Int

    @Query("SELECT COUNT(*) FROM user_cards WHERE userId = :userId AND cardId = :cardId")
    suspend fun getUserCardQuantity(userId: Long, cardId: Long): Int

    @Query("UPDATE user_cards SET quantity = quantity + 1 WHERE userId = :userId AND cardId = :cardId")
    suspend fun incrementUserCardQuantity(userId: Long, cardId: Long)

    @Query("INSERT OR REPLACE INTO user_cards (userId, cardId, quantity) VALUES (:userId, :cardId, 1)")
    suspend fun addCardToUser(userId: Long, cardId: Long)
}

data class CardWithQuantity(
    val id: Long,
    val name: String,
    val rarity: String,
    val imageUri: String,
    val description: String,
    val power: Int,
    val createdAt: Long,
    val quantity: Int
)
