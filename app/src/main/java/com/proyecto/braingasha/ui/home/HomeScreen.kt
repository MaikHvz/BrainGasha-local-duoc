package com.proyecto.braingasha.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.proyecto.braingasha.ui.home.HomeBody

@Composable
fun HomeScreen(navController: NavController, padding: PaddingValues) {
    // Pasamos el padding al HomeBody
    HomeBody(padding)
}
