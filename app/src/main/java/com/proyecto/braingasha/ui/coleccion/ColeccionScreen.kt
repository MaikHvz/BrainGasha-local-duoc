package com.proyecto.braingasha.ui.coleccion

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                // Usar key estable por cardId para evitar reciclaje incorrecto de estado
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
    // Asegurar que el estado y efecto se asocien al cardId, no al índice de la grilla
    val id = cardId.toIntOrNull() ?: (1..1025).random()
    var info by remember(cardId) { mutableStateOf<PokemonInfo?>(null) }
    var isLoading by remember(cardId) { mutableStateOf(true) }

    LaunchedEffect(cardId) {
        isLoading = true
        info = PokemonApi.fetchPokemon(id)
        isLoading = false
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(0.7f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Purpura),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
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

    val imageUrl = info?.imageUrl ?: officialArtworkUrl(id)
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = info?.name ?: "Pokémon",
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        contentScale = ContentScale.Fit
    )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Carta #$cardId",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

fun getRareza(cardId: String): String {
    return when (cardId.toInt() % 3) {
        0 -> "Común"
        1 -> "Rara"
        else -> "Épica"
    }
}

private fun officialArtworkUrl(id: Int): String {
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}
