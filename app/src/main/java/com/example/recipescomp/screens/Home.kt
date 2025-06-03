package com.example.recipescomp.screens


import com.example.recipescomp.components.BottomNavigationBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipescomp.components.RecipeCardFav
import com.example.recipescomp.data.category.Category
import com.example.recipescomp.data.category.CategoryItem
import com.example.recipescomp.data.local.AppDatabase
import com.example.recipescomp.data.local.FavoriteRecipesEntity
import com.example.recipescomp.data.repository.FavoriteRecipeRepository
import com.example.recipescomp.screens.favorites.FavoriteRecipeViewModel
import com.example.recipescomp.screens.favorites.FavoriteRecipeViewModelFactory
import com.example.recipescomp.ui.theme.BrownDark
import com.example.recipescomp.ui.theme.BrownLight


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Principal(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    val repository = remember { FavoriteRecipeRepository(db.FavoriteRecipesDao()) }
    val viewModel: FavoriteRecipeViewModel = viewModel(factory = FavoriteRecipeViewModelFactory(repository))

    val favorites by viewModel.favorites.collectAsState()
    val recipes = List(10) { index -> "Receta ${index + 1}" }

    var busqueda by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize().padding(top = 10.dp, bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
    )
    {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp, top = 16.dp)
        ) {
            item {
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
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Usuario",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(40.dp)
                                        .clickable(
                                            onClick = {
                                                navController.navigate("Perfil")
                                            }
                                        )
                                )
                            }
                        }

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
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
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
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            navController.navigate("recetasDestacadas")
                        }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Lista horizontal recetas destacadas
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(5) {
                        Box(
                            modifier = Modifier
                                .size(width = 200.dp, height = 140.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.LightGray)
                                .clickable {
                                    navController.navigate("Receta")
                                }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Título "Categorías"
            item {
                Text(
                    text = "Categorías",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrownDark,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Row con categorías (no se puede usar Row dentro de LazyColumn directamente, por eso usamos item)
            item {
                val categories = listOf(
                    Category(
                        "1", "Desayunos",
                        iconComposable = {
                            Icon(
                                imageVector = Icons.Default.FreeBreakfast,
                                contentDescription = "Desayuno",
                                modifier = Modifier.size(50.dp).
                                clickable {
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
                                modifier = Modifier.size(50.dp).
                                clickable {
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
                                modifier = Modifier.size(50.dp).
                                clickable {
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
                                modifier = Modifier.size(50.dp).
                                clickable {
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
                                modifier = Modifier.size(50.dp).
                                clickable {
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
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Título "Recomendaciones"
            item {
                Text(
                    text = "Recomendaciones",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrownDark,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            val recipes = List(10) { index -> "Receta ${index + 1}" }
            items(recipes) { title ->
                val isFavorite = favorites.any { it.name == title }

                RecipeCardFav(
                    title = title,
                    isFavoriteInitial = isFavorite,
                    navController = navController,
                    onFavoriteClick =  { isFav ->
                        if (isFav) {
                            viewModel.insertFavorite(FavoriteRecipesEntity(name = title, imageUrl = null))
                        } else {
                            viewModel.deleteFavorite(title)
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        // Barra inferior fija
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