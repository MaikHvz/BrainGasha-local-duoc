package com.proyecto.braingasha.ui.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.braingasha.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val context: Context
) : ViewModel() {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

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
                val savedEmail = prefs.getString("email", "")
                val savedPassword = prefs.getString("password", "")
                
                if (email == savedEmail && password == savedPassword) {
                    val user = User(
                        id = 1,
                        email = email,
                        password = password,
                        username = prefs.getString("username", "") ?: "",
                        profileImageUri = prefs.getString("profileImageUri", null),
                        coins = prefs.getInt("coins", 1000)
                    )
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
                val existingEmail = prefs.getString("email", "")
                if (email == existingEmail) {
                    _errorMessage.value = "El usuario ya existe"
                } else {
                    // Guardar datos en SharedPreferences
                    prefs.edit().apply {
                        putString("email", email)
                        putString("password", password)
                        putString("username", username)
                        putString("profileImageUri", null)
                        putInt("coins", 1000)
                        apply()
                    }
                    
                    val user = User(
                        id = 1,
                        email = email,
                        password = password,
                        username = username,
                        profileImageUri = null,
                        coins = 1000
                    )
                    _currentUser.value = user
                    _isLoggedIn.value = true
                }
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
                prefs.edit().putString("profileImageUri", imageUri).apply()
                _currentUser.value = user.copy(profileImageUri = imageUri)
            }
        }
    }

    fun updateUsername(username: String) {
        viewModelScope.launch {
            _currentUser.value?.let { user ->
                prefs.edit().putString("username", username).apply()
                _currentUser.value = user.copy(username = username)
            }
        }
    }
}
