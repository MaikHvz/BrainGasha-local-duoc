package com.proyecto.braingasha.ui.coleccion

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.proyecto.braingasha.ui.theme.Purpura
import com.proyecto.braingasha.ui.viewmodel.ColeccionViewModel
import com.proyecto.braingasha.data.network.PokemonApi
import com.proyecto.braingasha.data.network.PokemonInfo

@Composable
fun ColeccionScreen(viewModel: ColeccionViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val userCards = uiState.cards

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mi Colección",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Purpura,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if (userCards.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no tienes cartas en tu colección.\n¡Tira para conseguir algunas!",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(userCards, key = { it }) { cardId ->
                    CardItem(cardId = cardId)
                }
            }
        }
    }
}

@Composable
fun CardItem(cardId: String) {
    val context = LocalContext.current
    val id = cardId.toIntOrNull() ?: (1..1025).random()

    var info by remember(cardId) { mutableStateOf<PokemonInfo?>(null) }
    var isLoading by remember(cardId) { mutableStateOf(true) }
    var fallbackStage by remember(cardId) { mutableStateOf(0) }

    LaunchedEffect(cardId) {
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

    Card(
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(0.7f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Purpura),
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
                fontWeight = FontWeight.Bold,
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
                ) {
                    CircularProgressIndicator()
                }
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
                        // Avanza al siguiente fallback si falla la carga
                        fallbackStage = when (fallbackStage) {
                            0 -> 1
                            1 -> 2
                            else -> 2
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Carta #$cardId",
                fontSize = 14.sp,
                color = Color.Gray
            )
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
    // Usa nombre en minúsculas con guiones para PokemonDB
    // Si no hay nombre aún, retorna una URL vacía y dejará que Coil dispare onError
    return nameSlug?.let { "https://img.pokemondb.net/artwork/large/$it.jpg" } ?: ""
}
