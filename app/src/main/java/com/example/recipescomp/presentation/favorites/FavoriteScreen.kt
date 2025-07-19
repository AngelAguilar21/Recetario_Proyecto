package com.example.recipescomp.presentation.favorites

import android.net.Uri
import androidx.compose.foundation.lazy.items
import com.example.recipescomp.core.components.BackButton
import com.example.recipescomp.core.components.BottomNavigationBar
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.recipescomp.core.components.FavoriteRecipeCard
import com.example.recipescomp.data.local.AppDatabase
import com.example.recipescomp.domain.repository.FavoriteRecipeRepository
import com.example.recipescomp.core.theme.BrownDark
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ListFavRec(navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val repository = FavoriteRecipeRepository(db.FavoriteRecipesDao())
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    //Creacion de recetas
    val viewModel: FavoriteRecipeViewModel = viewModel(
        factory = FavoriteRecipeViewModelFactory(repository, currentUserId)
    )
    val favorites by viewModel.favorites.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 40.dp,
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingValues(start = 16.dp, end = 16.dp, bottom = 70.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BackButton(onClick = { navController.popBackStack() })
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Favorite Recipes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (favorites.isEmpty()) {
                // Empty state message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "You haven't added any recipes yet.",
                        fontSize = 18.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(32.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(favorites) { fav ->
                        FavoriteRecipeCard(
                            recipe = fav,
                            onClick = {
                                navController.navigate("receta/${Uri.encode(fav.mealId)}")
                            },
                            onDelete = { name ->
                                viewModel.deleteFavorite(name)
                            }
                        )
                    }
                }
            }
        }

        // 🔽 BARRA DE NAVEGACIÓN INFERIOR
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