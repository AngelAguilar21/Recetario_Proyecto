package com.example.recipescomp.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.recipescomp.ResourcesApi.MealViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.recipescomp.components.BottomNavigationBar
import com.example.recipescomp.ui.theme.BrownDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Principal(navController: NavController, viewModel: MealViewModel = viewModel()){

    val meals = viewModel.meals.value

    if (meals.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Cargando recetas...", color = Color.Gray)
        }
    }else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                ).background(Color(0xFFF5F5F5))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item{
                    header(navController = navController)
                }

                item {
                    FeaturedMeals(meals = meals, navController = navController)
                }

                item {
                    Categories(navController = navController)
                }

                item {
                    OtherMeals(meals = meals, navController = navController)
                }
            }

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
    }
}
