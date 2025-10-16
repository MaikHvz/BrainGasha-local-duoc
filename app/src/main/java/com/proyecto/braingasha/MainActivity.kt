package com.proyecto.braingasha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.proyecto.braingasha.ui.theme.BrainGashaTheme
import com.proyecto.braingasha.ui.main.MainScreen
import com.proyecto.braingasha.ui.login.LoginRegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                LoginRegisterScreen()   // 👈 Aquí llamas a tu Scaffold principal
            }
        }
    }
