package com.proyecto.braingasha.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val _user = MutableStateFlow(readUser())
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _totalPulls = MutableStateFlow(readTotalPulls())
    val totalPulls: StateFlow<Int> = _totalPulls.asStateFlow()

    private fun readUser(): User? {
        val email = prefs.getString("email", null)
        val password = prefs.getString("password", null)
        val username = prefs.getString("username", null)
        val profileImageUri = prefs.getString("profileImageUri", null)
        val coins = prefs.getInt("coins", 1000)
        val userCardsSet = prefs.getStringSet("user_cards", mutableSetOf()) ?: mutableSetOf()
        val totalCartas = prefs.getInt("total_cartas", userCardsSet.size)
        val totalTiradas = prefs.getInt("total_tiradas", prefs.getInt("total_pulls", 0))
        return if (email != null && password != null) {
            User(
                id = 1,
                email = email,
                password = password,
                username = username ?: "Usuario",
                profileImageUri = profileImageUri,
                coins = coins,
                totalCartas = totalCartas,
                totalTiradas = totalTiradas
            )
        } else {
            null
        }
    }

    private fun readTotalPulls(): Int = prefs.getInt("total_tiradas", prefs.getInt("total_pulls", 0))

    fun login(email: String, password: String): Boolean {
        val savedEmail = prefs.getString("email", "")
        val savedPassword = prefs.getString("password", "")
        return if (email == savedEmail && password == savedPassword) {
            _user.value = readUser()
            _totalPulls.value = readTotalPulls()
            true
        } else {
            false
        }
    }

    fun register(email: String, password: String, username: String) {
        prefs.edit().apply {
            putString("email", email)
            putString("password", password)
            putString("username", username)
            putString("profileImageUri", null)
            putInt("coins", 1000)
            putInt("total_tiradas", 0)
            putStringSet("user_cards", mutableSetOf())
            putInt("total_cartas", 0)
            putInt("total_pulls", 0)
        }.apply()
        _user.value = readUser()
        _totalPulls.value = 0
    }

    fun logout() {
        _user.value = null
    }

    fun updateProfileImage(imageUri: String) {
        prefs.edit().putString("profileImageUri", imageUri).apply()
        _user.value = _user.value?.copy(profileImageUri = imageUri)
    }

    fun updateUsername(username: String) {
        prefs.edit().putString("username", username).apply()
        _user.value = _user.value?.copy(username = username)
    }

    fun spendCoins(amount: Int): Boolean {
        val currentCoins = _user.value?.coins ?: 0
        if (currentCoins < amount) return false
        val newCoins = currentCoins - amount
        prefs.edit().putInt("coins", newCoins).apply()
        _user.value = _user.value?.copy(coins = newCoins)
        // Incrementar tiradas y persistir
        _totalPulls.value = _totalPulls.value + 1
        prefs.edit().apply {
            putInt("total_pulls", _totalPulls.value)
            putInt("total_tiradas", _totalPulls.value)
        }.apply()
        _user.value = _user.value?.copy(totalTiradas = _totalPulls.value)
        return true
    }

    // Nuevo: añadir monedas y actualizar _user y preferencias
    fun addCoins(amount: Int) {
        val currentCoins = _user.value?.coins ?: 0
        val newCoins = currentCoins + amount
        prefs.edit().putInt("coins", newCoins).apply()
        _user.value = _user.value?.copy(coins = newCoins)
    }

    fun addCard(cardId: String) {
        val currentCards = prefs.getStringSet("user_cards", mutableSetOf()) ?: mutableSetOf()
        if (!currentCards.contains(cardId)) {
            val updated = currentCards.toMutableSet().apply { add(cardId) }
            prefs.edit().putStringSet("user_cards", updated).apply()
            val totalCartas = updated.size
            prefs.edit().putInt("total_cartas", totalCartas).apply()
            _user.value = _user.value?.copy(totalCartas = totalCartas)
        }
    }

    fun getUserCards(): Set<String> = prefs.getStringSet("user_cards", mutableSetOf()) ?: mutableSetOf()
}