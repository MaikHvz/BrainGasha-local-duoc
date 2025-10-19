package com.proyecto.braingasha.ui.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.proyecto.braingasha.R
import com.proyecto.braingasha.ui.theme.*
import com.proyecto.braingasha.ui.viewmodel.AuthViewModel

@Composable
fun LoginRegisterScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit = {}
) {
    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()
    val isLoading by authViewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by authViewModel.errorMessage.collectAsStateWithLifecycle()

    // Si el usuario está logueado, navegar a la pantalla principal
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEF6B24)), // Naranja
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.85f),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isLogin) "Iniciar Sesión" else "Registrarse",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEF6B24)
                )

                // Mostrar error si existe
                errorMessage?.let { error ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
                    ) {
                        Text(
                            text = error,
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo de username solo en registro
                if (!isLogin) {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Nombre de usuario") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Mostrar solo en modo registro
                if (!isLogin) {
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirmar contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                Button(
                    onClick = {
                        authViewModel.clearError()
                        if (isLogin) {
                            authViewModel.login(email, password)
                        } else {
                            if (password == confirmPassword && username.isNotBlank()) {
                                authViewModel.register(email, password, username)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF6B24),
                        contentColor = Color.White
                    ),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Text(text = if (isLogin) "Entrar" else "Registrarse")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = { 
                    isLogin = !isLogin
                    authViewModel.clearError()
                }) {
                    Text(
                        text = if (isLogin)
                            "¿No tienes cuenta? Regístrate"
                        else
                            "¿Ya tienes cuenta? Inicia sesión"
                    )
                }
            }
        }
    }
}

