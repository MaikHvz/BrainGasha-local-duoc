package com.proyecto.braingasha.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.proyecto.braingasha.ui.components.BottomNavBar
import com.proyecto.braingasha.ui.components.TopBar
import com.proyecto.braingasha.ui.home.HomeScreen
import com.proyecto.braingasha.ui.coleccion.ColeccionScreen
import com.proyecto.braingasha.ui.profile.ProfileScreen
import com.proyecto.braingasha.ui.viewmodel.AuthViewModel

@Composable
fun MainScreen(
    authViewModel: AuthViewModel
) {
    // Controlador que maneja a qué pantalla estás navegando
    val navController = rememberNavController()

    // Scaffold principal
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavBar(navController) } // se lo pasamos al BottomNav
    ) { innerPadding ->
        // Aquí cambia el contenido (Home, Colección, o Perfil)
        NavHost(
            navController = navController,
            startDestination = "home", // Pantalla inicial
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(navController,innerPadding) } // ✅ ahora sí se pasa
            composable("coleccion") { ColeccionScreen() }
            composable("profile") { ProfileScreen(authViewModel = authViewModel) }
        }
    }
}
