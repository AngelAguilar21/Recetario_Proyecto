package com.example.recipescomp.screens

import android.graphics.Paint.Align
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FreeBreakfast
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.recipescomp.components.BottomNavigationBar
import com.example.recipescomp.ResourcesApi.MealViewModel
import com.example.recipescomp.data.category.Category
import com.example.recipescomp.data.category.CategoryItem
import com.example.recipescomp.ui.theme.BrownDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Principal(navController: NavController, viewModel: MealViewModel = viewModel()) {
    val meals = viewModel.meals.value

    var busqueda by remember { mutableStateOf("") }
    val favorites = remember { mutableStateListOf<String>() }

    // ⚠️ Mostrar mensaje si las recetas aún no se han cargado
    if (meals.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Cargando recetas...", color = Color.Gray)
        }
        return
    }

    // 🔀 Mezclar y dividir recetas en dos secciones
    val shuffledMeals = meals.shuffled()
    val featuredMeals = shuffledMeals.take(5)
    val remainingMeals = shuffledMeals.drop(5).take(13)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            ).background(Color(0xFFF5F5F5)) // ejemplo: gris claro
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {

            // 🟫 ENCABEZADO PRINCIPAL - Parte marrón superior
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(
                            BrownDark,
                            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .statusBarsPadding() // ✅ Corregido para evitar que tape el status bar
                            .padding(16.dp)
                    ) {
                        // 👤 Bienvenida + ícono de perfil
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Bienvenido",
                                    color = Color.White,
                                    fontSize = 35.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = "los ingredientes te esperan...",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Usuario",
                                    tint = Color.Gray,
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clickable {
                                            navController.navigate("Perfil")
                                        }
                                )
                            }
                        }

                        // 🔍 Barra de búsqueda + botón de filtro
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = busqueda,
                                onValueChange = { busqueda = it },
                                placeholder = { Text("Buscar", color = Color.Gray) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = "Ícono de búsqueda",
                                        tint = Color.Gray
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(2f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    // Acción al hacer clic
                                },
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(Color.White, RoundedCornerShape(12.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FormatListNumbered,
                                    contentDescription = "Ícono de búsqueda",
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            // ⭐ SECCIÓN 1: Recetas destacadas
            item {
                Text("Recetas destacadas", fontSize = 20.sp, modifier = Modifier.padding(16.dp),
                        color = BrownDark,
                    fontWeight = FontWeight.Bold)
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(featuredMeals) { meal ->
                        Box(
                            modifier = Modifier
                                .size(160.dp)
                                .shadow(6.dp, shape = RoundedCornerShape(20.dp)) // ✨ sombra ligera
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    navController.navigate("receta/${Uri.encode(meal.idMeal)}")
                                }
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(meal.strMealThumb),
                                contentDescription = meal.strMeal,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .align(Alignment.BottomCenter)
                                    .background(Color.White.copy(alpha = 0.8f)),
                                contentAlignment = Alignment.Center // ✅ texto centrado
                            ) {
                                Text(
                                    text = meal.strMeal,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }


            }

            // 📂 SECCIÓN 2: Categorías
            item {
                Spacer(modifier = Modifier.height(24.dp))

                // 🏷️ Título de categorías
                Text(
                    text = "Categorías",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrownDark,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                // 🔢 Lista horizontal de categorías
                val categories = listOf(
                    Category(
                        "1", "Desayunos",
                        iconComposable = {
                            Icon(
                                imageVector = Icons.Default.FreeBreakfast,
                                contentDescription = "Desayuno",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable {
                                        navController.navigate("desayuno")
                                    }
                            )
                        }
                    ),
                    Category(
                        "2", "Postres",
                        iconComposable = {
                            Icon(
                                imageVector = Icons.Default.Cake,
                                contentDescription = "Postres",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable {
                                        navController.navigate("postre")
                                    }
                            )
                        }
                    ),
                    Category(
                        "3", "Almuerzos",
                        iconComposable = {
                            Icon(
                                imageVector = Icons.Default.RestaurantMenu,
                                contentDescription = "Almuerzo",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable {
                                        navController.navigate("almuerzo")
                                    }
                            )
                        }
                    ),
                    Category(
                        "4", "Bebidas",
                        iconComposable = {
                            Icon(
                                imageVector = Icons.Default.LocalDrink,
                                contentDescription = "Bebidas",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable {
                                        navController.navigate("bebida")
                                    }
                            )
                        }
                    ),
                    Category(
                        "5", "Saludables",
                        iconComposable = {
                            Icon(
                                imageVector = Icons.Default.Spa,
                                contentDescription = "Saludable",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable {
                                        navController.navigate("saludable")
                                    }
                            )
                        }
                    )
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { category ->
                        CategoryItem(
                            icon = category.iconComposable,
                            categoryName = category.name
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }


            // 📦 SECCIÓN 3: Otras recetas
            item {
                Text("Otras recetas", fontSize = 20.sp, modifier = Modifier.padding(start = 16.dp),
                    color = BrownDark,
                    fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(remainingMeals) { meal ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .shadow(4.dp, shape = RoundedCornerShape(12.dp)) // ✨ sombra ligera
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .clickable {
                            navController.navigate("receta/${Uri.encode(meal.idMeal)}")
                        }
                        .padding(8.dp)
                ) {
                    // 📸 Imagen de la receta
                    Image(
                        painter = rememberAsyncImagePainter(meal.strMealThumb),
                        contentDescription = meal.strMeal,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // 📄 Info textual de la receta
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = meal.strMeal, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "Categoría: ${meal.strCategory ?: "Desconocida"}",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )

                        val ingredientCount = listOf(
                            meal.strIngredient1,
                            meal.strIngredient2,
                            meal.strIngredient3,
                            meal.strIngredient4,
                            meal.strIngredient5,
                            meal.strIngredient6,
                            meal.strIngredient7,
                            meal.strIngredient8,
                            meal.strIngredient9,
                            meal.strIngredient10
                        ).count { !it.isNullOrBlank() }

                        Text(
                            text = "Ingredientes: $ingredientCount",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }

                    // ❤️ Favorito
                    IconButton(
                        onClick = {
                            if (favorites.contains(meal.idMeal)) favorites.remove(meal.idMeal)
                            else favorites.add(meal.idMeal)
                        },
                        modifier = Modifier
                            .align(Alignment.Bottom)
                    ) {
                        Icon(
                            imageVector = if (favorites.contains(meal.idMeal)) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (favorites.contains(meal.idMeal)) Color.Red else Color.Gray
                        )
                    }
                }
            }
        }

        // ⬜ BOTTOM NAVIGATION BAR
        BottomNavigationBar(
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp) // 🔼 lo eleva del borde inferior
                .padding(horizontal = 32.dp) // 🔼 lo separa de los bordes laterales
                .clip(RoundedCornerShape(50)) // 🟢 redondeado total
                .background(BrownDark)
                .shadow(10.dp, RoundedCornerShape(50)) // ✨ sombra flotante
                .fillMaxWidth()
                .height(64.dp) // 📏 altura fija opcional,

        )
    }
}
