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
fun Login_Principal(navController: NavController){
    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") } // Para el registro
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Función para manejar el login
    fun handleLogin() {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                val result = FirebaseAuthManager.loginUser(email, password)
                isLoading = false

                if (result.isSuccess) {
                    Toast.makeText(context, "Login exitoso", Toast.LENGTH_SHORT).show()
                    navController.navigate("Principal") {
                        // Limpiar el stack de navegación
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Error en el login"
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(context, "Por favor, ingresa tu correo y contraseña", Toast.LENGTH_LONG).show()
        }
    }

    // Función para manejar el registro
    fun handleRegister() {
        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            if (password.length < 6) {
                Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_LONG).show()
                return
            }

            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                val result = FirebaseAuthManager.registerUser(name, email, password)
                isLoading = false

                if (result.isSuccess) {
                    Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    navController.navigate("home") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Error en el registro"
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            val message = if (isLogin) {
                "Por favor, ingresa tu correo y contraseña"
            } else {
                "Por favor, completa todos los campos"
            }
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
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
                .height(if (isLogin) 450.dp else 520.dp) // Más altura para registro
                .padding(horizontal = 40.dp)
                .align(Alignment.TopCenter)
                .offset(y = 250.dp),
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
                        isSelected = isLogin,
                        onClick = {
                            if (!isLoading) {
                                isLogin = true
                                // Limpiar campos al cambiar de modo
                                name = ""
                            }
                        },
                        modifier = Modifier.weight(1f),
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    ReusableButtonLogin(
                        text = "REGISTER",
                        isSelected = !isLogin,
                        onClick = {
                            if (!isLoading) {
                                isLogin = false
                            }
                        },
                        modifier = Modifier.weight(1f),
                        selectedColor = Color.LightGray,
                        unselectedColor = Color.LightGray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Black,
                        fontSize = 10.sp
                    )
                }

                ReusableText(
                    "WELCOME CHEF",
                    30.sp,
                    MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 30.dp),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo de nombre solo para registro
                if (!isLogin) {
                    ReusableLoginTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Full Name",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

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

                // Botón principal con loading
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    ReusableButton(
                        text = if (isLogin) "LOGIN" else "REGISTER",
                        onClick = {
                            if (!isLoading) {
                                if (isLogin) {
                                    handleLogin()
                                } else {
                                    handleRegister()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}