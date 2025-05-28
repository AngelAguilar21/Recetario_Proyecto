package com.example.recipescomp.screens


import BottomNavigationBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipescomp.data.category.Category
import com.example.recipescomp.data.category.CategoryItem
import com.example.recipescomp.data.category.Recipe
import com.example.recipescomp.ui.theme.BrownDark
import com.example.recipescomp.ui.theme.BrownLight
import com.example.recipescomp.ui.theme.GrayPlaceholder


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Principal(navController: NavController) {

    val scrollState = rememberScrollState()
    var busqueda by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(top = 40.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    BrownDark,
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Bienvenido",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "los ingredientes te esperan...",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Usuario",
                            tint = Color.Gray,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .background(Color(0xFFF7F2E7), RoundedCornerShape(12.dp))
                            .clickable {
                                navController.navigate("search") // Navega a la pantalla de búsqueda
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Ícono de búsqueda",
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Buscar",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Ícono adicional
                    IconButton(
                        onClick = {
                            // Acción adicional
                        },
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatListNumbered,
                            contentDescription = "Ícono adicional",
                            tint = Color.Gray
                        )
                    }
                }
            }
        }


        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recetas Destacadas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrownDark
                )
                Text(
                    text = "Ver todo >>",
                    fontSize = 14.sp,
                    color = BrownLight,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(5) {
                    Box(
                        modifier = Modifier
                            .size(width = 100.dp, height = 140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray)
                            .clickable {
                            navController.navigate("Receta")
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Categorías",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BrownDark
            )
            Spacer(modifier = Modifier.height(12.dp))

            val categories = listOf(
                Category(
                    "1", "Desayunos",
                    iconComposable = { Icon(imageVector = Icons.Default.FreeBreakfast, contentDescription = "Desayuno",modifier = Modifier.size(50.dp)) }
                ),
                Category(
                    "2", "Postres",
                    iconComposable = { Icon(imageVector = Icons.Default.Cake, contentDescription = "Postres",modifier = Modifier.size(50.dp)) }
                ),
                Category(
                    "3", "Almuerzos",
                    iconComposable = { Icon(imageVector = Icons.Default.RestaurantMenu, contentDescription = "Almuerzo", modifier = Modifier.size(50.dp)) }
                ),
                Category(
                    "4", "Bebidas",
                    iconComposable = { Icon(imageVector = Icons.Default.LocalDrink, contentDescription = "Bebidas",modifier = Modifier.size(50.dp)) }
                ),
                Category(
                    "5", "Saludables",
                    iconComposable = { Icon(imageVector = Icons.Default.Spa, contentDescription = "Saludable",modifier = Modifier.size(50.dp)) }
                )
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                categories.forEach { category ->
                    CategoryItem(
                        icon = category.iconComposable,
                        categoryName = category.name,
                        onClick = {
                            navController.navigate("Favoritos")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Recomendaciones",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BrownDark
            )
            Spacer(modifier = Modifier.height(12.dp))

            val popularRecipes = listOf(
                Recipe("p1", "Receta Popular 1"),
                Recipe("p2", "Receta Popular 2"),
                Recipe("p3", "Receta Popular 3"),
                Recipe("p4", "Receta Popular 4"),
                Recipe("p5", "Receta Popular 5")
            )

            LazyColumn(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(popularRecipes) { recipe ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(GrayPlaceholder, RoundedCornerShape(8.dp))
                            .clickable {
                                navController.navigate("Receta")
                            }
                    ) {
                        Text(
                            text = recipe.title,
                            modifier = Modifier.align(Alignment.Center),
                            color = BrownDark,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            BottomNavigationBar(
                navController = navController,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(BrownDark)
                    .padding(vertical = 16.dp)
            )
        }
    }
}

