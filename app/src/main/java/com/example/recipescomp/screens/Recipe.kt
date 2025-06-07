package com.example.recipescomp.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.recipescomp.ResourcesApi.Meal
import com.example.recipescomp.components.BackButton
import com.example.recipescomp.components.BottomNavigationBar
import com.example.recipescomp.components.ReusableButton
import com.example.recipescomp.ui.theme.BrownDark

@Composable
fun Receta(navController: NavController, meal: Meal) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("INGREDIENTES", "PASO A PASO")
    val context = LocalContext.current

    // Armar lista de ingredientes válidos
    val ingredientes = remember(meal) {
        (1..20).mapNotNull { i ->
            val ingredient = meal.javaClass.getDeclaredField("strIngredient$i").apply { isAccessible = true }.get(meal) as? String
            val measure = meal.javaClass.getDeclaredField("strMeasure$i").apply { isAccessible = true }.get(meal) as? String
            if (!ingredient.isNullOrBlank()) ingredient to (measure ?: "") else null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp, bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp)
                .padding(horizontal = 10.dp, vertical = 16.dp)
        ) {
            BackButton(onClick = { navController.popBackStack() })

            Image(
                painter = rememberImagePainter(meal.strMealThumb),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = meal.strMeal,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            meal.strCategory?.let {
                Text("Categoría: $it", style = MaterialTheme.typography.bodySmall)
            }

            meal.strArea?.let {
                Text("Región: $it", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = BrownDark
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) BrownDark else Color.Gray
                            )
                        }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                when (selectedTab) {
                    0 -> {
                        items(ingredientes) { (name, measure) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(Color(0xFFF7F2E7), RoundedCornerShape(8.dp))
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(name)
                                Text(measure, color = Color(0xFFE1B38B))
                            }
                        }
                    }

                    1 -> {
                        val pasos = meal.strInstructions?.split(Regex("\r?\n"))?.filter { it.isNotBlank() } ?: listOf("No hay instrucciones disponibles.")

                        items(pasos) { paso ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier.padding(vertical = 6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(BrownDark, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "${pasos.indexOf(paso) + 1}",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(paso)
                            }
                        }
                    }
                }
            }

            if (!meal.strYoutube.isNullOrBlank()) {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(meal.strYoutube))
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrownDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                ) {
                    Text("Ver Video", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ReusableButton(
                "Modo Cocina",
                onClick = { navController.navigate("modoCocina") },
                modifier = Modifier.padding(horizontal = 30.dp)
            )
        }

        BottomNavigationBar(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(BrownDark)
                .padding(vertical = 16.dp)
        )
    }
}
