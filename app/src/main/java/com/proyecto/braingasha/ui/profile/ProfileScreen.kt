package com.proyecto.braingasha.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.proyecto.braingasha.R
import com.proyecto.braingasha.data.entity.User
import com.proyecto.braingasha.ui.theme.*
import com.proyecto.braingasha.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onLogout: () -> Unit = {}
) {
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()
    val isLoading by authViewModel.isLoading.collectAsStateWithLifecycle()
    
    var showEditDialog by remember { mutableStateOf(false) }
    var newUsername by remember { mutableStateOf(TextFieldValue("")) }
    var showImageSourceDialog by remember { mutableStateOf(false) }
    
    // Permisos para cámara
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )
    
    // Launcher para seleccionar imagen de galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { authViewModel.updateProfileImage(it.toString()) }
    }
    
    // Launcher para tomar foto con cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // La imagen se guarda automáticamente en el URI proporcionado
        }
    }
    
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header con botón de logout
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Perfil",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFEF6B24)
            )
            
            Button(
                onClick = {
                    authViewModel.logout()
                    onLogout()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text("Cerrar Sesión")
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Foto de perfil
        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            AsyncImage(
                model = currentUser?.profileImageUri?.let { 
                    ImageRequest.Builder(LocalContext.current)
                        .data(it)
                        .crossfade(true)
                        .build()
                } ?: R.drawable.custom,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color(0xFFEF6B24), CircleShape),
                contentScale = ContentScale.Crop
            )
            
            // Botón para cambiar imagen
            FloatingActionButton(
                onClick = {
                    // Mostrar opciones: cámara o galería
                    showImageSourceDialog = true
                },
                modifier = Modifier.size(40.dp),
                containerColor = Color(0xFFEF6B24),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.CameraAlt, contentDescription = "Cambiar foto")
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Información del usuario
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Usuario:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = currentUser?.username ?: "Sin nombre",
                            fontSize = 16.sp
                        )
                        IconButton(onClick = { 
                            newUsername = TextFieldValue(currentUser?.username ?: "")
                            showEditDialog = true 
                        }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar")
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Email:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = currentUser?.email ?: "Sin email",
                        fontSize = 16.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Monedas:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${currentUser?.coins ?: 0}",
                        fontSize = 16.sp,
                        color = Color(0xFFEF6B24),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Estadísticas
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Estadísticas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "0", // TODO: Implementar conteo de cartas
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF6B24)
                        )
                        Text("Cartas", fontSize = 14.sp)
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "0", // TODO: Implementar conteo de tiradas
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF6B24)
                        )
                        Text("Tiradas", fontSize = 14.sp)
                    }
                }
            }
        }
    }
    
    // Dialog para editar username
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Editar Usuario") },
            text = {
                OutlinedTextField(
                    value = newUsername,
                    onValueChange = { newUsername = it },
                    label = { Text("Nombre de usuario") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        authViewModel.updateUsername(newUsername.text)
                        showEditDialog = false
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    // Dialog para seleccionar fuente de imagen
    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Seleccionar Imagen") },
            text = { Text("¿De dónde quieres seleccionar la imagen?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (cameraPermissionState.status.isGranted) {
                            // TODO: Implementar toma de foto
                            galleryLauncher.launch("image/*")
                        } else {
                            cameraPermissionState.launchPermissionRequest()
                        }
                        showImageSourceDialog = false
                    }
                ) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cámara")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        galleryLauncher.launch("image/*")
                        showImageSourceDialog = false
                    }
                ) {
                    Icon(Icons.Filled.PhotoLibrary, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Galería")
                }
            }
        )
    }
}

