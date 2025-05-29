package com.example.recipescomp.screens

import com.example.recipescomp.components.BackButton
import com.example.recipescomp.components.BottomNavigationBar
import com.example.recipescomp.components.RecipeCardFav
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipescomp.ui.theme.BrownDark

@Composable
fun ListFavRec(navController: NavController) {
    //Creacion de recetas
    val recipes = List(10) { index ->
        "Receta ${index + 1}"
    }
    Box(modifier = Modifier.fillMaxSize().padding(top = 10.dp, bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, bottom = 70.dp)
        ) {
            BackButton(onClick = { navController.popBackStack() })

            Text(
                text=" Recetas Favoritas",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = BrownDark,
                modifier = Modifier
                    .padding(bottom = 20.dp, top = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(recipes) { title->
                    RecipeCardFav(
                        title = title,
                        true,
                        navController = navController,
                        onFavoriteClick = { isFav ->
                            // Manejo de favorito si quieres
                        }
                    )
                }
            }
        }

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
