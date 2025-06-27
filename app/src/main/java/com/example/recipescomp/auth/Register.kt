package com.example.recipescomp.auth

import android.widget.Toast
import com.example.recipescomp.components.ReusableButton
import com.example.recipescomp.components.ReusableButtonLogin
import com.example.recipescomp.components.ReusableLoginTextField
import com.example.recipescomp.components.ReusableText
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipescomp.R
import com.example.recipescomp.data.Firebase.FirebaseAuthManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Registrarse(navController: NavController) {
    var isRegistry by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Función para validar email
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Función para manejar el registro
    fun handleRegister() {
        // Validaciones
        when {
            name.trim().isEmpty() -> {
                Toast.makeText(context, "Por favor, ingresa tu nombre", Toast.LENGTH_LONG).show()
                return
            }
            email.trim().isEmpty() -> {
                Toast.makeText(context, "Por favor, ingresa tu correo", Toast.LENGTH_LONG).show()
                return
            }
            !isValidEmail(email.trim()) -> {
                Toast.makeText(context, "Por favor, ingresa un correo válido", Toast.LENGTH_LONG).show()
                return
            }
            password.isEmpty() -> {
                Toast.makeText(context, "Por favor, ingresa tu contraseña", Toast.LENGTH_LONG).show()
                return
            }
            password.length < 6 -> {
                Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_LONG).show()
                return
            }
        }

        isLoading = true
        CoroutineScope(Dispatchers.Main).launch {
            val result = FirebaseAuthManager.registerUser(name.trim(), email.trim(), password)
            isLoading = false

            if (result.isSuccess) {
                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                navController.navigate("Principal") {
                    // Limpiar el stack de navegación
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            } else {
                val error = result.exceptionOrNull()?.message ?: "Error en el registro"
                // Personalizar mensajes de error más amigables
                val friendlyError = when {
                    error.contains("email-already-in-use") -> "Este correo ya está registrado"
                    error.contains("weak-password") -> "La contraseña es muy débil"
                    error.contains("invalid-email") -> "El formato del correo no es válido"
                    error.contains("network-request-failed") -> "Error de conexión. Verifica tu internet"
                    else -> "Error en el registro: $error"
                }
                Toast.makeText(context, friendlyError, Toast.LENGTH_LONG).show()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_login_2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(520.dp) // Altura fija para el registro
                .padding(horizontal = 40.dp)
                .align(Alignment.TopCenter)
                .offset(y = 200.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Botones de LOGIN/REGISTER
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ReusableButtonLogin(
                        text = "LOGIN",
                        isSelected = false,
                        onClick = {
                            if (!isLoading) {
                                navController.navigate("Login_Principal")
                            }
                        },
                        modifier = Modifier.weight(1f),
                        selectedColor = Color.LightGray,
                        unselectedColor = Color.LightGray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Black,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    ReusableButtonLogin(
                        text = "REGISTER",
                        isSelected = isRegistry,
                        onClick = {
                            if (!isLoading) {
                                isRegistry = true
                            }
                        },
                        modifier = Modifier.weight(1f),
                        fontSize = 10.sp
                    )
                }

                ReusableText(
                    "WELCOME CHEF",
                    30.sp,
                    MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 15.dp),
                    color = Color.Black
                )

                ReusableText(
                    "Create your account",
                    14.sp,
                    MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 25.dp),
                    color = Color.Gray
                )

                // Campo de nombre
                ReusableLoginTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Full Name",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Campo de email
                ReusableLoginTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Campo de contraseña
                ReusableLoginTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    modifier = Modifier.fillMaxWidth(),
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Botón de registro con loading
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    ReusableButton(
                        text = "REGISTER",
                        onClick = {
                            if (!isLoading) {
                                handleRegister()
                            }
                        }
                    )
                }
            }
        }
    }
}