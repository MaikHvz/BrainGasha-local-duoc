package com.proyecto.braingasha.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.braingasha.ui.coleccion.ColeccionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ColeccionViewModel(
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val _uiState = MutableStateFlow(ColeccionUiState())
    val uiState: StateFlow<ColeccionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Observa directamente el flujo de cartas para reflejar la colección al instante
            authViewModel.userCardsFlow.collect { cards ->
                _uiState.value = ColeccionUiState(cards = cards)
            }
        }
    }
}
