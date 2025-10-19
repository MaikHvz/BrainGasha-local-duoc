package com.proyecto.braingasha.ui.coleccion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.proyecto.braingasha.data.dao.CardWithQuantity
import com.proyecto.braingasha.ui.theme.*
import com.proyecto.braingasha.ui.viewmodel.AuthViewModel

@Composable
fun ColeccionScreen(
    authViewModel: AuthViewModel
) {
    val userCards by authViewModel.getUserCards().collectAsStateWithLifecycle(initialValue = emptyList())
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Mi Colección",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFEF6B24),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        if (userCards.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No tienes cartas aún",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "¡Ve a la pantalla principal y haz tu primera tirada!",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(userCards) { cardWithQuantity ->
                    CardItem(cardWithQuantity = cardWithQuantity)
                }
            }
        }
    }
}

@Composable
fun CardItem(cardWithQuantity: CardWithQuantity) {
    val rarityColor = when (cardWithQuantity.rarity) {
        "Legendary" -> Color(0xFFFFD700)
        "Epic" -> Color(0xFF9C27B0)
        "Rare" -> Color(0xFF2196F3)
        "Common" -> Color(0xFF4CAF50)
        else -> Color.Gray
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Rarity indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(rarityColor, RoundedCornerShape(4.dp))
                    .padding(4.dp)
            ) {
                Text(
                    text = cardWithQuantity.rarity,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Card name
            Text(
                text = cardWithQuantity.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Power
            Text(
                text = "Poder: ${cardWithQuantity.power}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Quantity
            if (cardWithQuantity.quantity > 1) {
                Text(
                    text = "x${cardWithQuantity.quantity}",
                    fontSize = 12.sp,
                    color = Color(0xFFEF6B24),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
