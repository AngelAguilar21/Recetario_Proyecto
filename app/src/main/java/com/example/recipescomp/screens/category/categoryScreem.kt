package com.example.recipescomp.screens.category

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.recipescomp.resourcesApi.MealViewModel
import com.example.recipescomp.components.BackButton
import com.example.recipescomp.ui.theme.BrownDark
import com.example.recipescomp.components.BottomNavigationBar

@Composable
fun CategoryScreen(category: String, navController: NavController) {
    val mealViewModel: MealViewModel = viewModel()
    val meals = mealViewModel.meals.value

    // Llamamos al ViewModel para obtener las recetas de la categoría seleccionada
    LaunchedEffect(category) {
        mealViewModel.fetchMealsByCategory(category)
    }
    Spacer(modifier = Modifier.height(30.dp))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(), bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()) // Separar el contenido de la barra de estado
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Botón para navegar atrás
            BackButton(onClick = { navController.popBackStack() })

            // Título de la categoría
            Text(
                text = category,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = BrownDark,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Mostrar un indicador de carga si no hay recetas
            if (meals.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                // Mostrar las recetas en LazyColumn
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(meals) { meal ->
                        // Caja para cada receta
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    // Navegar a la pantalla de detalles de la receta
                                    navController.navigate("receta/${Uri.encode(meal.idMeal)}")
                                }
                        ) {
                            // Imagen de la receta
                            Image(
                                painter = rememberAsyncImagePainter(meal.strMealThumb),
                                contentDescription = meal.strMeal,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .shadow(6.dp, RoundedCornerShape(16.dp))
                            )

                            // Nombre de la receta en la parte inferior
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .align(Alignment.BottomCenter)
                                    .background(Color.White.copy(alpha = 0.8f)),
                                contentAlignment = Alignment.Center
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
        }

        // Barra de navegación inferior
        BottomNavigationBar(
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp) // Lo eleva del borde inferior
                .padding(horizontal = 32.dp) // Lo separa de los bordes laterales
                .clip(RoundedCornerShape(50)) // Redondeado
                .background(BrownDark)
                .shadow(10.dp, RoundedCornerShape(50)) // Sombra flotante
                .fillMaxWidth()
                .height(64.dp)
        )
    }
}
