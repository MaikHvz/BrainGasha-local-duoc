package com.proyecto.braingasha.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.proyecto.braingasha.ui.sparks.SparksScreen
import com.proyecto.braingasha.ui.tienda.TiendaScreen
import com.proyecto.braingasha.ui.viewmodel.AuthViewModel
import com.proyecto.braingasha.ui.viewmodel.ColeccionViewModel
import com.proyecto.braingasha.ui.navigation.Routes

@Composable
fun MainScreen(
    authViewModel: AuthViewModel
) {
    // Controlador que maneja a qué pantalla estás navegando
    val navController = rememberNavController()

    // Scaffold principal
    Scaffold(
        topBar = { TopBar(authViewModel) },
        bottomBar = { BottomNavBar(navController) } // se lo pasamos al BottomNav
    ) { innerPadding ->
        // Aquí cambia el contenido (Home, Colección, o Perfil)
        NavHost(
            navController = navController,
            startDestination = Routes.HOME, // Pantalla inicial
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) { HomeScreen(navController, innerPadding, authViewModel) }
            composable(Routes.SPARKS) { SparksScreen() }
            composable(Routes.COLECCION) {
                val coleccionViewModel = remember(authViewModel) { ColeccionViewModel(authViewModel) }
                ColeccionScreen(coleccionViewModel)
            }
            composable(Routes.TIENDA) { TiendaScreen() }
            composable(Routes.PROFILE) { ProfileScreen(authViewModel = authViewModel) }
        }
    }
}
