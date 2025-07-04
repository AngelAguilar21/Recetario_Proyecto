package com.example.recipescomp.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipescomp.R
import com.example.recipescomp.components.*
import com.example.recipescomp.data.Firebase.FirebaseAuthManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Login_Principal(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    fun showSnackbar(message: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    fun handleLogin() {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                val result = FirebaseAuthManager.loginUser(email, password)
                isLoading = false

                if (result.isSuccess) {
                    navController.navigate("Principal") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Error en el login"
                    showSnackbar(error)
                }
            }
        } else {
            showSnackbar("Por favor, ingresa tu correo y contraseña")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
                    top = paddingValues.calculateTopPadding()
                )
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
                    .height(450.dp)
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
                    ReusableText(
                        "WELCOME CHEF",
                        30.sp,
                        MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 30.dp),
                        color = Color.Black,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ReusableLoginTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    ReusableLoginTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        modifier = Modifier.fillMaxWidth(),
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    if (isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        ReusableButton(
                            text = "LOGIN",
                            onClick = {
                                if (!isLoading) {
                                    handleLogin()
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(onClick = {
                        navController.navigate("Registrarse")
                    }) {
                        Text("Don't have an account? Sign up", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
