package com.proyecto.braingasha.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.proyecto.braingasha.ui.home.HomeBody
import com.proyecto.braingasha.ui.viewmodel.AuthViewModel
import com.proyecto.braingasha.ui.viewmodel.HomeViewModel
import com.proyecto.braingasha.ui.navigation.Routes

@Composable
fun HomeScreen(navController: NavController, padding: PaddingValues, authViewModel: AuthViewModel) {
    val homeViewModel = remember(authViewModel) { HomeViewModel(authViewModel) }
    HomeBody(padding, homeViewModel) { navController.navigate(Routes.COLECCION) }
}
