package com.proyecto.braingasha.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.proyecto.braingasha.ui.home.HomeBody
import com.proyecto.braingasha.ui.viewmodel.AuthViewModel

@Composable
fun HomeScreen(navController: NavController, padding: PaddingValues, authViewModel: AuthViewModel) {
    // Pasamos el padding, el viewModel y el navController al HomeBody
    HomeBody(padding, authViewModel, navController)
}
