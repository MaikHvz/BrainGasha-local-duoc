package com.proyecto.braingasha.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.braingasha.data.User
import com.proyecto.braingasha.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.delay

class AuthViewModel(
    private val context: Context
) : ViewModel() {
    
    // Reemplaza acceso directo a SharedPreferences por repositorio
    private val repository = AuthRepository(context)

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    private val _totalPulls = MutableStateFlow(0)
    val totalPulls: StateFlow<Int> = _totalPulls.asStateFlow()

    private var autoCoinJob: Job? = null
    
    init {
        // Sincroniza estado con el repositorio
        viewModelScope.launch {
            repository.user.collect { user ->
                _currentUser.value = user
                _isLoggedIn.value = user != null
            }
        }
        viewModelScope.launch {
            repository.totalPulls.collect { pulls ->
                _totalPulls.value = pulls
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val success = repository.login(email, password)
                if (!success) {
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
                repository.register(email, password, username)
            } catch (e: Exception) {
                _errorMessage.value = "Error al registrarse: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        stopAutoCoinIncrement()
        repository.logout()
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun updateProfileImage(imageUri: String) {
        viewModelScope.launch { repository.updateProfileImage(imageUri) }
    }

    fun updateUsername(username: String) {
        viewModelScope.launch { repository.updateUsername(username) }
    }
    
    fun spendCoins(amount: Int): Boolean {
        return repository.spendCoins(amount)
    }
    
    fun addCard(cardId: String) {
        repository.addCard(cardId)
    }
    
    fun getUserCards(): Set<String> {
        return repository.getUserCards()
    }

    // Nuevo: exponer addCoins
    fun addCoins(amount: Int) {
        viewModelScope.launch { repository.addCoins(amount) }
    }

    // Nuevo: iniciar incremento automático cada 10 segundos (+100)
    fun startAutoCoinIncrement() {
        if (autoCoinJob != null) return // evitar múltiples jobs
        autoCoinJob = viewModelScope.launch {
            while (isActive) {
                delay(10_000)
                if (_isLoggedIn.value) {
                    repository.addCoins(100)
                }
            }
        }
    }

    // Nuevo: detener incremento automático explícitamente
    fun stopAutoCoinIncrement() {
        autoCoinJob?.cancel()
        autoCoinJob = null
    }
}
