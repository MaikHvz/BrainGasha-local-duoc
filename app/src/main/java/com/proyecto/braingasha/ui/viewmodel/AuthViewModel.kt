package com.proyecto.braingasha.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.braingasha.data.entity.User
import com.proyecto.braingasha.data.database.AppDatabase
import com.proyecto.braingasha.data.database.DatabaseInitializer
import com.proyecto.braingasha.data.repository.UserRepository
import com.proyecto.braingasha.data.repository.CardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val context: Context
) : ViewModel() {
    
    private val database = AppDatabase.getDatabase(context)
    private val userRepository = UserRepository(database.userDao())
    private val cardRepository = CardRepository(database.cardDao())
    private val databaseInitializer = DatabaseInitializer(database.cardDao())
    
    init {
        // Inicializar la base de datos con cartas de ejemplo
        viewModelScope.launch {
            databaseInitializer.initializeDatabase()
        }
    }

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val user = userRepository.login(email, password)
                if (user != null) {
                    _currentUser.value = user
                    _isLoggedIn.value = true
                } else {
                    _errorMessage.value = "Credenciales incorrectas"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al iniciar sesión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(email: String, password: String, username: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val user = User(
                    email = email,
                    password = password,
                    username = username
                )
                
                val result = userRepository.register(user)
                result.fold(
                    onSuccess = { userId ->
                        _currentUser.value = user.copy(id = userId)
                        _isLoggedIn.value = true
                    },
                    onFailure = { error ->
                        _errorMessage.value = error.message ?: "Error al registrarse"
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = "Error al registrarse: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _isLoggedIn.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun updateProfileImage(imageUri: String) {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                userRepository.updateUserProfileImage(user.id, imageUri)
                _currentUser.value = user.copy(profileImageUri = imageUri)
            }
        }
    }

    fun updateUsername(username: String) {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                userRepository.updateUsername(user.id, username)
                _currentUser.value = user.copy(username = username)
            }
        }
    }
    
    // Métodos para el sistema gacha
    fun drawCard() {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                if (user.coins >= 100) {
                    val drawnCard = cardRepository.drawRandomCard(user.id)
                    if (drawnCard != null) {
                        // Reducir monedas
                        userRepository.updateUserCoins(user.id, user.coins - 100)
                        _currentUser.value = user.copy(coins = user.coins - 100)
                    }
                }
            }
        }
    }
    
    fun getUserCards() = cardRepository.getUserCardsFlow(_currentUser.value?.id ?: 0)
}
