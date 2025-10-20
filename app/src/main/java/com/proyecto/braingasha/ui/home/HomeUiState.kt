package com.proyecto.braingasha.ui.home

data class HomeUiState(
    val coins: Int = 0,
    val totalPulls: Int = 0,
    val userCards: Int = 0,
    val canPull: Boolean = true
)