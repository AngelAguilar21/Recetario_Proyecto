package com.example.recipescomp.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.Icon
import androidx.compose.foundation.lazy.items


data class FavoriteItem(
    val id: Int,
    val title: String,
    val creator: String,
    val imageCount: Int = 5
)

@Composable
fun Favoritos(navController: NavController) {
    val items = listOf(
        FavoriteItem(1, "Resultado de Búsqueda 1", "Creador 1"),
        FavoriteItem(2, "Resultado de Búsqueda 2", "Creador 2"),
        FavoriteItem(3, "Resultado de Búsqueda 3", "Creador 3")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(8.dp)
    ) {
        items(items) { item ->
            FavoriteItemCard(navController, item)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun FavoriteItemCard(navController: NavController, item: FavoriteItem) {
    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(15.dp)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(item.imageCount) { index ->
                    Box(
                        modifier = Modifier
                            .size(width = 100.dp, height = 140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray)
                            .clickable {
                                navController.navigate("Receta/${item.id}/$index")
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                item.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                Text(
                    item.creator,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}