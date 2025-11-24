package com.proyecto.braingasha.ui.tienda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.proyecto.braingasha.data.network.PokemonApi
import com.proyecto.braingasha.data.network.PokemonInfo
import com.proyecto.braingasha.ui.theme.Purpura
import com.proyecto.braingasha.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun TiendaScreen(authViewModel: AuthViewModel) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val userCoins = currentUser?.coins ?: 0

    var offers by remember { mutableStateOf(listOf<Int>()) }

    // Genera 4 IDs únicos entre 1..1025 y refresca cada 20 segundos
    LaunchedEffect(Unit) {
        while (true) {
            offers = (1..1025).shuffled().take(4)
            delay(20_000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tienda de Cartas",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Purpura,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Text(
            text = "Cada carta: 500 monedas",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(offers, key = { it }) { id ->
                StoreCardItem(
                    id = id,
                    canBuy = userCoins >= 500,
                    onBuy = {
                        val ok = authViewModel.spendCoins(500)
                        if (ok) {
                            authViewModel.addCard(id.toString())
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun StoreCardItem(id: Int, canBuy: Boolean, onBuy: () -> Unit) {
    val context = LocalContext.current
    var info by remember(id) { mutableStateOf<PokemonInfo?>(null) }
    var isLoading by remember(id) { mutableStateOf(true) }
    var fallbackStage by remember(id) { mutableStateOf(0) }

    LaunchedEffect(id) {
        isLoading = true
        info = PokemonApi.fetchPokemon(id)
        isLoading = false
    }

    val nameSlug = info?.name
        ?.lowercase()
        ?.replace(" ", "-")
        ?.replace(".", "")
        ?.replace("'", "")

    val imageUrl = when (fallbackStage) {
        0 -> info?.imageUrl?.takeIf { it.isNotBlank() } ?: officialArtworkUrl(id)
        1 -> spriteUrl(id)
        else -> pokemonDbArtworkUrl(nameSlug)
    }

    // Borde arcoiris tipo neón
    val rainbowBrush = Brush.sweepGradient(
        listOf(
            Color(0xFFFF33CC), // magenta fluor
            Color(0xFFFF9900), // naranja
            Color(0xFFFFFF00), // amarillo
            Color(0xFF33FF33), // verde fluor
            Color(0xFF33FFFF), // cian
            Color(0xFF3366FF), // azul
            Color(0xFFFF33CC)  // magenta
        )
    )

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .aspectRatio(0.75f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(3.dp, rainbowBrush),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = info?.name ?: "Pokémon #$id",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = Purpura
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = info?.name ?: "Pokémon",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentScale = ContentScale.Fit,
                    onError = {
                        fallbackStage = when (fallbackStage) { 0 -> 1; 1 -> 2; else -> 2 }
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Precio: 500 monedas",
                fontSize = 14.sp,
                color = Color.Gray
            )

            // Botón llamativo (simulación de gradiente con borde neón)
            Button(
                onClick = onBuy,
                enabled = canBuy,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary, // naranja del menú
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(44.dp)
            ) {
                Text(text = if (canBuy) "Comprar" else "GG", fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun officialArtworkUrl(id: Int): String {
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}

private fun spriteUrl(id: Int): String {
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}

private fun pokemonDbArtworkUrl(nameSlug: String?): String {
    return nameSlug?.let { "https://img.pokemondb.net/artwork/large/$it.jpg" } ?: ""
}
