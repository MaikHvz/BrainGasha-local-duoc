package com.proyecto.braingasha.data

import android.content.Context
import com.proyecto.braingasha.data.remote.NetworkModule
import com.proyecto.braingasha.data.remote.dto.LoginRequest
import com.proyecto.braingasha.data.remote.dto.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthRepository(private val context: Context) {
    private val api = NetworkModule.authApi

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _totalPulls = MutableStateFlow(0)
    val totalPulls: StateFlow<Int> = _totalPulls

    suspend fun login(email: String, password: String): Boolean {
        val response = api.login(LoginRequest(email, password))
        return if (response.isSuccessful) {
            response.body()?.let { dto ->
                val mapped = User(
                    id = dto.id ?: 0,
                    email = dto.email,
                    password = null,
                    username = dto.username,
                    profileImageUri = dto.profileImageUri,
                    coins = dto.coins ?: 1000,
                    totalCartas = dto.totalCartas ?: 0,
                    totalTiradas = dto.totalTiradas ?: 0
                )
                _user.value = mapped
                _totalPulls.value = mapped.totalTiradas
            }
            true
        } else {
            false
        }
    }

    suspend fun register(email: String, password: String, username: String): Boolean {
        val response = api.register(RegisterRequest(username, email, password))
        return response.isSuccessful
    }

    fun logout() {
        _user.value = null
        _totalPulls.value = 0
    }

    fun isLoggedIn(): Boolean = _user.value != null

    fun spendCoins(amount: Int): Boolean {
        val current = _user.value ?: return false
        if (current.coins < amount) return false
        val newCoins = current.coins - amount
        val newTiradas = _totalPulls.value + 1
        _user.value = current.copy(coins = newCoins, totalTiradas = newTiradas)
        _totalPulls.value = newTiradas
        return true
    }

    fun addCoins(amount: Int) {
        val current = _user.value ?: return
        _user.value = current.copy(coins = current.coins + amount)
    }

    fun addCard(cardId: String) {
        val current = _user.value ?: return
        val newTotal = current.totalCartas + 1
        _user.value = current.copy(totalCartas = newTotal)
    }

    fun getUserCards(): Set<String> = emptySet()

    fun getUserCardsList(): List<String> = emptyList()

    fun updateProfileImage(imageUri: String) {
        val current = _user.value ?: return
        _user.value = current.copy(profileImageUri = imageUri)
    }

    fun updateUsername(username: String) {
        val current = _user.value ?: return
        _user.value = current.copy(username = username)
    }
}