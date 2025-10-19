package com.proyecto.braingasha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyecto.braingasha.ui.theme.BrainGashaTheme
import com.proyecto.braingasha.ui.main.MainScreen
import com.proyecto.braingasha.ui.login.LoginRegisterScreen
import com.proyecto.braingasha.ui.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BrainGashaTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel { AuthViewModel(context) }
    val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()
    
    if (isLoggedIn) {
        MainScreen(authViewModel = authViewModel)
    } else {
        LoginRegisterScreen(authViewModel = authViewModel)
    }
}
