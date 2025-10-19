package com.proyecto.braingasha.data.repository

import com.proyecto.braingasha.data.dao.UserDao
import com.proyecto.braingasha.data.entity.User
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userDao: UserDao
) {
    suspend fun login(email: String, password: String): User? {
        return userDao.login(email, password)
    }

    suspend fun register(user: User): Result<Long> {
        return try {
            // Verificar si el usuario ya existe
            val existingUser = userDao.getUserByEmail(user.email)
            if (existingUser != null) {
                Result.failure(Exception("El usuario ya existe"))
            } else {
                val userId = userDao.insertUser(user)
                Result.success(userId)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }

    fun getUserByIdFlow(userId: Long): Flow<User?> {
        return userDao.getUserByIdFlow(userId)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun updateUserCoins(userId: Long, coins: Int) {
        userDao.updateUserCoins(userId, coins)
    }

    suspend fun updateUserProfileImage(userId: Long, imageUri: String?) {
        userDao.updateUserProfileImage(userId, imageUri)
    }

    suspend fun updateUsername(userId: Long, username: String) {
        userDao.updateUsername(userId, username)
    }
}
