package com.example.recipescomp.screens.home

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.recipescomp.ResourcesApi.Meal
import com.example.recipescomp.ui.theme.BrownDark

@Composable
fun FeaturedMeals(meals: List<Meal>, navController: NavController) {

    val shuffledMeals = meals.shuffled()
    val featuredMeals = shuffledMeals.take(5)

    Row{
        Text("Recetas destacadas", fontSize = 20.sp, modifier = Modifier.padding(16.dp),
            color = BrownDark, fontWeight = FontWeight.Bold)

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

}
