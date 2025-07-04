package com.example.recipescomp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.recipescomp.components.BackButton
import com.example.recipescomp.components.BottomNavigationBar
import com.example.recipescomp.data.local.AppDatabase
import com.example.recipescomp.data.repository.FavoriteRecipeRepository
import com.example.recipescomp.screens.favorites.FavoriteRecipeViewModel
import com.example.recipescomp.screens.favorites.FavoriteRecipeViewModelFactory
import com.example.recipescomp.ui.theme.BrownDark
import com.example.recipescomp.data.Firebase.FirebaseAuthManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun Perfil(navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val repository = FavoriteRecipeRepository(db.FavoriteRecipesDao())
    val viewModel: FavoriteRecipeViewModel = viewModel(factory = FavoriteRecipeViewModelFactory(repository))
    val favorites by viewModel.favorites.collectAsState()

    // Firebase Auth y Firestore
    val currentUser = FirebaseAuthManager.getCurrentUser()
    val firestore = FirebaseFirestore.getInstance()

    // Estados para el usuario
    var userName by remember { mutableStateOf("Usuario") }
    var userEmail by remember { mutableStateOf("") }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Obtener datos del usuario desde Firestore
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            userEmail = user.email ?: ""
            try {
                val userDoc = firestore.collection("users").document(user.uid).get().await()
                if (userDoc.exists()) {
                    userName = userDoc.getString("name") ?: user.email?.substringBefore("@") ?: "Usuario"
                } else {
                    userName = user.email?.substringBefore("@") ?: "Usuario"
                }
            } catch (e: Exception) {
                userName = user.email?.substringBefore("@") ?: "Usuario"
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 40.dp,
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingValues(start = 16.dp, end = 16.dp, bottom = 70.dp))
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BackButton(onClick = { navController.popBackStack() })
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Profile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }

                // Botón de cerrar sesión
                IconButton(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier
                        .background(
                            Color.Red.copy(alpha = 0.1f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Cerrar sesión",
                        tint = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Avatar",
                        modifier = Modifier.size(72.dp),
                        tint = Color.DarkGray
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = userName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    if (userEmail.isNotEmpty()) {
                        Text(
                            text = userEmail,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Favorite recipes",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Favoritas",
                    tint = Color.Yellow
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(favorites) { fav ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(100.dp)
                            .padding(vertical = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(100.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.LightGray)
                                .clickable {
                                    navController.navigate("receta/${fav.mealId}")
                                }

                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(fav.imageUrl),
                                contentDescription = fav.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = fav.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }

        // 🔽 BARRA DE NAVEGACIÓN INFERIOR
        BottomNavigationBar(
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
                .padding(horizontal = 32.dp)
                .clip(RoundedCornerShape(50))
                .background(BrownDark)
                .shadow(10.dp, RoundedCornerShape(50))
                .fillMaxWidth()
                .height(64.dp)
        )
    }

    // Diálogo de confirmación para cerrar sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text("Cerrar sesión")
            },
            text = {
                Text("¿Estás seguro de que quieres cerrar sesión?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            try {
                                FirebaseAuthManager.signOut()
                                showLogoutDialog = false
                                // Navegar a la pantalla de login y limpiar el back stack
                                navController.navigate("Login_Principal") {
                                    popUpTo(0) { inclusive = true }
                                }
                            } catch (e: Exception) {
                                showLogoutDialog = false
                            }
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Text("Cerrar sesión")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}