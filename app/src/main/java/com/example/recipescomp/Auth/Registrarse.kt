package com.example.recipescomp.Auth

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipescomp.R
import androidx.compose.ui.text.input.PasswordVisualTransformation


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
            .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
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
                .height(750.dp)
                .padding(horizontal = 40.dp)
                .align(Alignment.TopCenter)
                .offset(y = 80.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
        )  {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            navController.navigate("Login_Principal")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                    ) {
                        Text("INGRESAR",
                            fontSize = 13.sp,
                            color = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { isRegistry = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRegistry) Color(0xFF6B3E15) else Color.LightGray,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("REGISTRARSE",
                            fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "BIENVENIDO CHEF",
                    fontSize = 30.sp,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 15.dp)
                )

                Text(
                    text = "Ingrese su información",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Nombre de Usuario") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = dietExpanded,
                    onExpandedChange = { dietExpanded = !dietExpanded },
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = selectedDiet,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo de dieta") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = dietExpanded)
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = dietExpanded,
                        onDismissRequest = { dietExpanded = false }
                    ) {
                        dietOptions.forEachIndexed { index, option ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = {
                                    selectedDiet = option
                                    dietExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = cookingLevelExpanded,
                    onExpandedChange = { cookingLevelExpanded = !cookingLevelExpanded },
                ) {
                    OutlinedTextField(
                        value = selectedCookingLevel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Nivel de Cocina") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = cookingLevelExpanded)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = cookingLevelExpanded,
                        onDismissRequest = { cookingLevelExpanded = false }
                    ) {
                        cookingLevelOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedCookingLevel = option
                                    cookingLevelExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Dropdown Cocina Favorita
                ExposedDropdownMenuBox(
                    expanded = favoriteCuisineExpanded,
                    onExpandedChange = { favoriteCuisineExpanded = !favoriteCuisineExpanded },
                ) {
                    OutlinedTextField(
                        value = selectedFavoriteCuisine,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Cocina Favorita") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = favoriteCuisineExpanded)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = favoriteCuisineExpanded,
                        onDismissRequest = { favoriteCuisineExpanded = false }
                    ) {
                        favoriteCuisineOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedFavoriteCuisine = option
                                    favoriteCuisineExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        navController.navigate("Login_Principal")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B3E15))
                ){
                    Text("REGISTRARSE", color = Color.White)
                }
            }

        }
    }
}