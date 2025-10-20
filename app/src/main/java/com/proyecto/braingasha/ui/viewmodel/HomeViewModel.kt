package com.proyecto.braingasha.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.braingasha.ui.home.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.delay

class HomeViewModel(
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val pullMutex = Mutex()
    private val cooldownMillis: Long = 800 // tiempo de espera entre tiradas

    init {
        viewModelScope.launch {
            combine(authViewModel.currentUser, authViewModel.totalPulls) { user, totalPulls ->
                val coins = user?.coins ?: 0
                val cardsCount = user?.totalCartas ?: 0
                HomeUiState(coins = coins, totalPulls = totalPulls, userCards = cardsCount, canPull = _uiState.value.canPull)
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun onPull() {
        viewModelScope.launch {
            // Evitar reentradas concurrentes
            pullMutex.withLock {
                if (!_uiState.value.canPull) return@withLock
                // Inicia cooldown y deshabilita el botón
                _uiState.value = _uiState.value.copy(canPull = false)

                // Costo de tirar una carta: 100 monedas
                val success = authViewModel.spendCoins(100)
                if (success) {
                    // Generar una carta aleatoria (ID entre 1 y 10), permite duplicados
                    val randomCardId = (1..10).random().toString()
                    authViewModel.addCard(randomCardId)
                    // Refrescar conteo: incrementa localmente mientras llega el flujo
                    _uiState.value = _uiState.value.copy(userCards = _uiState.value.userCards + 1)
                }

                // Espera antes de permitir otra tirada
                delay(cooldownMillis)
                _uiState.value = _uiState.value.copy(canPull = true)
            }
        }
    }
}