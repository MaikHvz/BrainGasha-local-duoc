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

@Composable
fun BottomNavBar(navController: NavController) {
    // Para marcar el item seleccionado
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = MaterialTheme.colorScheme.background) {

        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") },
            icon = { Icon(painterResource(R.drawable.home), contentDescription = "Home",modifier = Modifier.size(50.dp)) }
        )

        NavigationBarItem(
            selected = currentRoute == "sparks",
            onClick = { navController.navigate("sparks") },
            icon = { Icon(painterResource(R.drawable.sparkles), contentDescription = "Sparks",modifier = Modifier.size(50.dp))
            }
        )

        NavigationBarItem(
            selected = currentRoute == "coleccion",
            onClick = { navController.navigate("coleccion") },
            icon = { Icon(painterResource(R.drawable.grid), contentDescription = "Colección",modifier = Modifier.size(50.dp)) }
        )

        NavigationBarItem(
            selected = currentRoute == "tienda",
            onClick = { navController.navigate("tienda") },
            icon = { Icon(painterResource(R.drawable.shop), contentDescription = "Tienda",modifier = Modifier.size(50.dp)) }
        )

        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { navController.navigate("profile") },
            icon = { Icon(painterResource(R.drawable.custom), contentDescription = "Perfil",modifier = Modifier.size(50.dp)) }
        )
    }
}
