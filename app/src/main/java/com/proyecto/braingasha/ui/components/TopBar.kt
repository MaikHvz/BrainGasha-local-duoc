package com.proyecto.braingasha.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Paid

import androidx.compose.material3.TopAppBar



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Text(
                text = "BrainGasha",
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
                IconButton(onClick = { /* acción de notificación */ }) {
                    Icon(
                        imageVector = Icons.Filled.Paid,
                        contentDescription = "Monedas",
                        tint = Color.White
                    )
                }
                Text(
                    text = "900",
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

@Preview(showBackground = true)
@Composable
fun PreviewTopBar() {
    TopBar()
}
