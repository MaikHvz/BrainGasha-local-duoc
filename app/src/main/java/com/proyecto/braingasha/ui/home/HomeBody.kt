package com.proyecto.braingasha.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecto.braingasha.R
import com.proyecto.braingasha.ui.theme.*
import com.proyecto.braingasha.ui.viewmodel.HomeViewModel

@Composable
fun HomeBody(innerPadding: PaddingValues, viewModel: HomeViewModel, onViewCollection: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val userCoins = uiState.coins
    val totalPulls = uiState.totalPulls
    val userCards = uiState.userCards

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Imagen principal
        Image(
            painter = painterResource(id = R.drawable.heropet),
            contentDescription = "Hero pet image",
            modifier = Modifier.size(300.dp)
        )

        // Títulos
        Text(
            text = "Colecciona Tus",
            color = Negro,
            fontWeight = FontWeight.SemiBold,
            fontSize = 45.sp
        )
        Text(
            text = "Cartas Épicas",
            color = Purpura,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 45.sp
        )

        Text(
            text = "Obtén cartas únicas, amplía tu colección y conviértete en un maestro",
            fontWeight = FontWeight.SemiBold,
            color = Gris,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(280.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Sección de estadísticas
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(CelesteFondo)
                .padding(vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$userCards",
                        fontWeight = FontWeight.Bold,
                        color = Purpura,
                        fontSize = 22.sp
                    )
                    Text(
                        text = "Cartas\nColeccionadas",
                        textAlign = TextAlign.Center,
                        color = Gris,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$totalPulls",
                        fontWeight = FontWeight.ExtraBold,
                        color = Purpura,
                        fontSize = 22.sp
                    )
                    Text(
                        text = "Tiradas\nTotales",
                        textAlign = TextAlign.Center,
                        color = Gris,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            // Botones
            Button(
                onClick = { viewModel.onPull() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purpura,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(45.dp),
                enabled = userCoins >= 100 // Deshabilitar si no hay suficientes monedas
            ) {
                Text(
                    text = "Tirar (100 monedas)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedButton(
                onClick = { onViewCollection() },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(45.dp),
                border = BorderStroke(2.dp, Purpura),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Purpura
                )
            ) {
                Text(
                    text = "Ver Colección",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }


    }
}

