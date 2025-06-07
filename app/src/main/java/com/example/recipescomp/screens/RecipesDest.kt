package com.example.recipescomp.screens

import com.example.recipescomp.components.BackButton
import com.example.recipescomp.components.BottomNavigationBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipescomp.data.category.Recipe
import com.example.recipescomp.ui.theme.BrownDark
import com.example.recipescomp.ui.theme.GrayPlaceholder

@Composable
fun RecetasDestacadas(navController: NavController){
    Box(modifier = Modifier.fillMaxSize().padding(top = 10.dp, bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, bottom = 70.dp)
        ) {
            BackButton(onClick = { navController.popBackStack() })

            Text(
                text="Recetas Destacadas",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = BrownDark,
                modifier = Modifier
                    .padding(bottom = 20.dp, top = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )

            val popularRecipes = listOf(
                Recipe("p1", "Receta Popular 1"),
                Recipe("p2", "Receta Popular 2"),
                Recipe("p3", "Receta Popular 3"),
                Recipe("p4", "Receta Popular 4"),
                Recipe("p5", "Receta Popular 5")
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(popularRecipes) { recipe ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
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
