package com.proyecto.braingasha.data.dao

import androidx.room.*
import com.proyecto.braingasha.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): User?

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserByIdFlow(userId: Long): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Query("UPDATE users SET coins = :coins WHERE id = :userId")
    suspend fun updateUserCoins(userId: Long, coins: Int)

    @Query("UPDATE users SET profileImageUri = :imageUri WHERE id = :userId")
    suspend fun updateUserProfileImage(userId: Long, imageUri: String?)

    @Query("UPDATE users SET username = :username WHERE id = :userId")
    suspend fun updateUsername(userId: Long, username: String)
}
