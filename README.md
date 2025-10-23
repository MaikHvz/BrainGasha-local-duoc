# BrainGasha - App de Cartas Gacha

Una aplicación móvil de cartas gacha de pokemon desarrollada con Kotlin, Jetpack Compose.

## Características Implementadas

### 🔐 Sistema de Autenticación
- **Login/Registro funcional** con SQLite
- Validación de credenciales
- Manejo de errores y estados de carga.
- Navegación automática entre login y app principal

### 👤 Perfil de Usuario
- **Cambio de imagen de perfil** desde cámara o galería
- Edición de nombre de usuario
- Visualización de estadísticas (monedas, cartas, tiradas)
- Botón de cerrar sesión

### 🎮 Sistema de Cartas
- **Base de datos local sstorage** 
- Entidades: User
- Sistema de rarezas: Common, Rare, Epic, Legendary

### 🏗️ Arquitectura
- **MVVM** con ViewModels
- **Hilt** para inyección de dependencias
- **Jetpack Compose** para UI
- **Navigation Compose** para navegación

## Estructura del Proyecto



## Funcionalidades

### Pantallas Implementadas
1. **Login/Registro** - Autenticación de usuarios
2. **Home** - Pantalla principal con estadísticas
3. **Colección** - Visualización de cartas del usuario
4. **Perfil** - Gestión de perfil de usuario

### Navegación
- Bottom Navigation Bar con 5 secciones
- Navegación automática basada en estado de autenticación
- Transiciones suaves entre pantallas

### Base de Datos
- **Usuarios**: email, password, username, profileImageUri, coins

## Tecnologías Utilizadas

- **Kotlin** - Lenguaje de programación
- **Jetpack Compose** - Framework de UI
- - **Material 3** - Framework de UI
- **Hilt** - Inyección de dependencias
- **Navigation Compose** - Navegación


## Permisos Requeridos

- `CAMERA` - Para tomar fotos de perfil
- `READ_EXTERNAL_STORAGE` - Para acceder a galería
- `WRITE_EXTERNAL_STORAGE` - Para guardar imágenes

## Instalación

1. Clona el repositorio
2. Abre el proyecto en Android Studio
3. Sincroniza las dependencias de Gradle
4. Ejecuta la aplicación

## Próximas Funcionalidades

- [ ] Tienda de monedas
- [ ] Sistema de logros
- [ ] Animaciones de cartas
- [ ] Modo offline completo
- [ ] Sincronización en la nube

