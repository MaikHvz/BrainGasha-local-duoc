package com.proyecto.braingasha.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Paid
import com.proyecto.braingasha.ui.viewmodel.AuthViewModel
import androidx.compose.material3.TopAppBar
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.layout.layout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(authViewModel: AuthViewModel? = null) {
    TopAppBar(
        title = {
            Text(
                text = "PokeGacha",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        },
        actions = {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 20.dp)
            ) {
                val currentUserState = authViewModel?.currentUser?.collectAsState()
                val userCoins = currentUserState?.value?.coins ?: 0

                // Estado para detectar aumentos y mostrar overlay
                var prevCoins by remember { mutableStateOf(userCoins) }
                var showOverlay by remember { mutableStateOf(false) }

                LaunchedEffect(userCoins) {
                    if (userCoins > prevCoins) {
                        showOverlay = true
                        prevCoins = userCoins
                        kotlinx.coroutines.delay(900)
                        showOverlay = false
                    } else {
                        prevCoins = userCoins
                    }
                }

                val overlayAlpha by animateFloatAsState(
                    targetValue = if (showOverlay) 1f else 0f,
                    label = "overlayAlpha"
                )
                val overlayOffset by animateDpAsState(
                    targetValue = if (showOverlay) (-6).dp else 0.dp,
                    label = "overlayOffset"
                )

                // Ícono con overlay absoluto a la izquierda
                Box {
                    IconButton(onClick = { /* acción de notificación */ }) {
                        Icon(
                            imageVector = Icons.Filled.Paid,
                            contentDescription = "Monedas",
                            tint = Color.White
                        )
                    }

                    if (overlayAlpha > 0f) {
                        val density = LocalDensity.current
                        val overlayXpx = with(density) { 24.dp.toPx() }.toInt() // hacia la izquierda del ícono
                        val overlayYpx = with(density) { overlayOffset.toPx() }.toInt()

                        Text(
                            text = "+100",
                            color = Color(0xFFFFFFFF),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .alpha(overlayAlpha)
                                .layout { measurable, constraints ->
                                    val placeable = measurable.measure(constraints)
                                    layout(0, 0) {
                                        placeable.place(-overlayXpx, overlayYpx)
                                    }
                                }
                        )
                    }
                }

                Text(
                    text = "$userCoins",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors
            (
            containerColor = Color(0xFFEF6B24),
            titleContentColor = Color.White
        )
    )
}

