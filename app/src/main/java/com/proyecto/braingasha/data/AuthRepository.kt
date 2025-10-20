package com.proyecto.braingasha.data

import android.content.Context
import android.content.SharedPreferences
import com.proyecto.braingasha.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthRepository(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val _user = MutableStateFlow(readUser())
    val user: StateFlow<User?> = _user

    private val _totalPulls = MutableStateFlow(prefs.getInt("total_pulls", 0))
    val totalPulls: StateFlow<Int> = _totalPulls

    private fun readUser(): User? {
        val email = prefs.getString("email", null)
        val password = prefs.getString("password", null)
        val username = prefs.getString("username", null)
        val profileImageUri = prefs.getString("profileImageUri", null)
        val coins = prefs.getInt("coins", 1000)
        val userCardsListStr = prefs.getString("user_cards_list", "")
        val userCardsList = if (userCardsListStr.isNullOrBlank()) emptyList() else userCardsListStr.split(",").filter { it.isNotBlank() }
        val totalCartas = prefs.getInt("total_cartas", userCardsList.size)
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

    fun login(email: String, password: String): Boolean {
        val storedEmail = prefs.getString("email", null)
        val storedPassword = prefs.getString("password", null)
        return if (storedEmail == email && storedPassword == password) {
            _user.value = readUser()
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
            putString("user_cards_list", "")
            putInt("total_cartas", 0)
            putInt("total_pulls", 0)
        }.apply()
        _user.value = readUser()
        _totalPulls.value = 0
    }

    fun logout() {
        prefs.edit().apply {
            remove("email")
            remove("password")
            remove("username")
            remove("profileImageUri")
            remove("coins")
            remove("user_cards")
            remove("user_cards_list")
            remove("total_cartas")
            remove("total_tiradas")
            remove("total_pulls")
        }.apply()
        _user.value = null
        _totalPulls.value = 0
    }

    fun isLoggedIn(): Boolean = _user.value != null

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
        // Mantener set de únicas para pantallas que lo necesiten
        val currentCards = prefs.getStringSet("user_cards", mutableSetOf()) ?: mutableSetOf()
        val updated = currentCards.toMutableSet().apply { add(cardId) }
        prefs.edit().putStringSet("user_cards", updated).apply()
        // Añadir a lista con duplicados
        val listStr = prefs.getString("user_cards_list", "") ?: ""
        val newListStr = if (listStr.isBlank()) cardId else "$listStr,$cardId"
        prefs.edit().putString("user_cards_list", newListStr).apply()
        // Incrementar total_cartas en 1 para contar duplicados también
        val currentTotal = prefs.getInt("total_cartas", 0)
        val newTotal = currentTotal + 1
        prefs.edit().putInt("total_cartas", newTotal).apply()
        _user.value = _user.value?.copy(totalCartas = newTotal)
    }

    fun getUserCards(): Set<String> = prefs.getStringSet("user_cards", mutableSetOf()) ?: mutableSetOf()

    fun getUserCardsList(): List<String> {
        val listStr = prefs.getString("user_cards_list", "") ?: ""
        return if (listStr.isBlank()) emptyList() else listStr.split(",").filter { it.isNotBlank() }
    }

    fun updateProfileImage(imageUri: String) {
        prefs.edit().putString("profileImageUri", imageUri).apply()
        _user.value = _user.value?.copy(profileImageUri = imageUri)
    }

    fun updateUsername(username: String) {
        prefs.edit().putString("username", username).apply()
        _user.value = _user.value?.copy(username = username)
    }
}