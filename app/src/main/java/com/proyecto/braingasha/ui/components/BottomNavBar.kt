package com.proyecto.braingasha.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.proyecto.braingasha.R
import com.proyecto.braingasha.ui.navigation.Routes

@Composable
fun BottomNavBar(navController: NavController) {
    // Para marcar el item seleccionado
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = MaterialTheme.colorScheme.background) {

        NavigationBarItem(
            selected = currentRoute == Routes.HOME,
            onClick = { navController.navigate(Routes.HOME) },
            icon = { Icon(painterResource(R.drawable.home), contentDescription = "Home",modifier = Modifier.size(50.dp)) }
        )

        NavigationBarItem(
            selected = currentRoute == Routes.SPARKS,
            onClick = { navController.navigate(Routes.SPARKS) },
            icon = { Icon(painterResource(R.drawable.sparkles), contentDescription = "Sparks",modifier = Modifier.size(50.dp))
            }
        )

        NavigationBarItem(
            selected = currentRoute == Routes.COLECCION,
            onClick = { navController.navigate(Routes.COLECCION) },
            icon = { Icon(painterResource(R.drawable.grid), contentDescription = "Colección",modifier = Modifier.size(50.dp)) }
        )

        NavigationBarItem(
            selected = currentRoute == Routes.TIENDA,
            onClick = { navController.navigate(Routes.TIENDA) },
            icon = { Icon(painterResource(R.drawable.shop), contentDescription = "Tienda",modifier = Modifier.size(50.dp)) }
        )

        NavigationBarItem(
            selected = currentRoute == Routes.PROFILE,
            onClick = { navController.navigate(Routes.PROFILE) },
            icon = { Icon(painterResource(R.drawable.custom), contentDescription = "Perfil",modifier = Modifier.size(50.dp)) }
        )
    }
}
