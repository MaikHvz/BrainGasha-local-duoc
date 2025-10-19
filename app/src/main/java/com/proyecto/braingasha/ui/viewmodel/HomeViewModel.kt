package com.proyecto.braingasha.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.braingasha.ui.home.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(authViewModel.currentUser, authViewModel.totalPulls) { user, totalPulls ->
                val coins = user?.coins ?: 0
                val cardsCount = authViewModel.getUserCards().size
                HomeUiState(coins = coins, totalPulls = totalPulls, userCards = cardsCount)
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun onPull() {
        // Costo de tirar una carta: 100 monedas
        val success = authViewModel.spendCoins(100)
        if (success) {
            // Generar una carta aleatoria (ID entre 1 y 10)
            val randomCardId = (1..10).random().toString()
            authViewModel.addCard(randomCardId)
            // Refrescar conteo de cartas en el estado
            _uiState.value = _uiState.value.copy(userCards = authViewModel.getUserCards().size)
        }
    }
}