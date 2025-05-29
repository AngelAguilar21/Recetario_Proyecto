package com.example.recipescomp.auth

import com.example.recipescomp.components.ReusableButton
import com.example.recipescomp.components.ReusableButtonLogin
import com.example.recipescomp.components.ReusableDropdown
import com.example.recipescomp.components.ReusableLoginTextField
import com.example.recipescomp.components.ReusableText
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipescomp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Registrarse(navController: NavController ) {
    var isRegistry by remember { mutableStateOf(true) }

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var selectedDiet by remember { mutableStateOf("") }
    var dietExpanded by remember { mutableStateOf(false) }
    val dietOptions = listOf("Omnívora", "Vegetariana", "Vegana")

    var selectedCookingLevel by remember { mutableStateOf("") }
    var cookingLevelExpanded by remember { mutableStateOf(false) }
    val cookingLevelOptions = listOf("Principiante", "Intermedio", "Avanzado")

    var selectedFavoriteCuisine by remember { mutableStateOf("") }
    var favoriteCuisineExpanded by remember { mutableStateOf(false) }
    val favoriteCuisineOptions = listOf("Italiana", "Peruana", "China", "Mexicana")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_login_2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 40.dp)
                .align(Alignment.TopCenter)
                .offset(y = 80.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
        ) {
            LazyColumn(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // El Row con los botones
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ReusableButtonLogin(
                            text = "INGRESAR",
                            isSelected = false,
                            onClick = { navController.navigate("Login_Principal") },
                            modifier = Modifier.weight(1f),
                            selectedColor = Color.LightGray,
                            unselectedColor = Color.LightGray,
                            selectedTextColor = Color.Black,
                            unselectedTextColor = Color.Black,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        ReusableButtonLogin(
                            text = "REGISTRARSE",
                            isSelected = isRegistry,
                            onClick = { isRegistry = true },
                            modifier = Modifier.weight(1f),
                            fontSize = 10.sp
                        )
                    }
                }

                // El resto de los elementos como items separados con espacios automáticos
                item {
                    ReusableText(
                        "BIENVENIDO CHEF", 25.sp, MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 15.dp), color = Color.Black
                    )
                }

                item {
                    ReusableText(
                        "Ingrese su informacion",
                        14.sp,
                        MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 15.dp),
                        color = Color.Gray
                    )
                }

                item {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Nombre de Usuario") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item { Spacer(modifier = Modifier.height(12.dp)) }

                item {
                    ReusableLoginTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Nombre de Usuario",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item { Spacer(modifier = Modifier.height(12.dp)) }

                item {
                    ReusableLoginTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Contraseña",
                        modifier = Modifier.fillMaxWidth(),
                        isPassword = true
                    )
                }

                item { Spacer(modifier = Modifier.height(12.dp)) }

                item {
                    ReusableDropdown(
                        label = "Tipo de Dieta",
                        options = dietOptions,
                        selected = selectedDiet,
                        onSelect = { selectedDiet = it },
                        expanded = dietExpanded,
                        onExpandedChange = { dietExpanded = it }
                    )
                }

                item { Spacer(modifier = Modifier.height(12.dp)) }

                item {
                    ReusableDropdown(
                        label = "Nivel de cocina",
                        options = cookingLevelOptions,
                        selected = selectedCookingLevel,
                        expanded = cookingLevelExpanded,
                        onSelect = { selectedCookingLevel = it },
                        onExpandedChange = { cookingLevelExpanded = it }
                    )
                }

                item { Spacer(modifier = Modifier.height(12.dp)) }

                item {
                    ReusableDropdown(
                        label = "Cocina Favorita",
                        options = favoriteCuisineOptions,
                        selected = selectedFavoriteCuisine,
                        expanded = favoriteCuisineExpanded,
                        onSelect = { selectedFavoriteCuisine = it },
                        onExpandedChange = { favoriteCuisineExpanded = it }
                    )
                }

                item { Spacer(modifier = Modifier.height(10.dp)) }

                item {
                    ReusableButton(
                        "REGISTRARSE",
                        onClick = { navController.navigate("Login_Principal") })
                }
            }
        }
    }
}
