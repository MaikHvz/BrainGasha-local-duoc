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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.proyecto.braingasha.R
import com.proyecto.braingasha.ui.theme.*
import com.proyecto.braingasha.ui.viewmodel.AuthViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

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
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    fun isValidGmail(mail: String): Boolean {
        val regex = Regex("^[A-Za-z0-9._%+-]+@gmail\\.com$")
        return regex.matches(mail)
    }

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
                    onValueChange = {
                        email = it
                        emailError = if (email.isNotEmpty() && !isValidGmail(email)) "Ingresa un Gmail válido (ej. usuario@gmail.com)" else null
                    },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(imageVector = Icons.Filled.Email, contentDescription = "Correo") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = emailError != null,
                    supportingText = {
                        if (emailError != null) Text(emailError!!, color = Color.Red, fontSize = 12.sp)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo de username solo en registro
                if (!isLogin) {
                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            username = it
                            usernameError = if (username.isNotEmpty() && username.length < 3) "Mínimo 3 caracteres" else null
                        },
                        label = { Text("Nombre de usuario") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        leadingIcon = { Icon(imageVector = Icons.Filled.Person, contentDescription = "Usuario") },
                        isError = usernameError != null,
                        supportingText = {
                            if (usernameError != null) Text(usernameError!!, color = Color.Red, fontSize = 12.sp)
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = if (password.isNotEmpty() && password.length < 6) "Mínimo 6 caracteres" else null
                    },
                    label = { Text("Contraseña") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = "Contraseña") },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = passwordError != null,
                    supportingText = {
                        if (passwordError != null) Text(passwordError!!, color = Color.Red, fontSize = 12.sp)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Mostrar solo en modo registro
                if (!isLogin) {
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            confirmPasswordError = if (confirmPassword.isNotEmpty() && confirmPassword != password) "Las contraseñas no coinciden" else null
                        },
                        label = { Text("Confirmar contraseña") },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = "Confirmar contraseña") },
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = confirmPasswordError != null,
                        supportingText = {
                            if (confirmPasswordError != null) Text(confirmPasswordError!!, color = Color.Red, fontSize = 12.sp)
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                Button(
                    onClick = {
                        authViewModel.clearError()
                        if (isLogin) {
                            authViewModel.login(email, password)
                        } else {
                            val isEmailValid = isValidGmail(email)
                            val isUsernameValid = username.length >= 3
                            val isPasswordValid = password.length >= 6
                            val isConfirmValid = password == confirmPassword

                            emailError = if (!isEmailValid) "Ingresa un Gmail válido (ej. usuario@gmail.com)" else null
                            usernameError = if (!isUsernameValid) "Mínimo 3 caracteres" else null
                            passwordError = if (!isPasswordValid) "Mínimo 6 caracteres" else null
                            confirmPasswordError = if (!isConfirmValid) "Las contraseñas no coinciden" else null

                            if (isEmailValid && isUsernameValid && isPasswordValid && isConfirmValid) {
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

