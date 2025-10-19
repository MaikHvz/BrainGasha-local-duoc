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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.braingasha.ui.theme.Purpura
import com.proyecto.braingasha.ui.viewmodel.AuthViewModel

@Composable
fun ColeccionScreen(authViewModel: AuthViewModel) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val userCards = authViewModel.getUserCards().toList()
    
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
                items(userCards) { cardId ->
                    CardItem(cardId = cardId)
                }
            }
        }
    }
}

@Composable
fun CardItem(cardId: String) {
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Carta #$cardId",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Purpura
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Rareza: ${getRareza(cardId)}",
                    fontSize = 14.sp
                )
            }
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
