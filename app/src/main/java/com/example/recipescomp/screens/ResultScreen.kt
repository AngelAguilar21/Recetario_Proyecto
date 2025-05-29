package com.example.recipescomp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipescomp.components.Recipe
import com.example.recipescomp.components.RecipeItem
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.recipescomp.components.FilterSheetContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(navController: NavController) {
    val recipes = listOf(
        Recipe("Resultado de Búsqueda 1", "Creador 1", "60'", "8"),
        Recipe("Resultado de Búsqueda 2", "Creador 2", "45'", "5"),
        Recipe("Resultado de Búsqueda 3", "Creador 3", "30'", "3")
    )

    var showFilters by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Lanzar el ModalBottomSheet cuando showFilters sea true
    if (showFilters) {
        ModalBottomSheet(
            onDismissRequest = { showFilters = false },
            sheetState = sheetState
        ) {
            FilterSheetContent(
                onApplyFilters = { showFilters = false },
                onClearFilters = { /* Lógica para limpiar los filtros */ }
            )
        }
    }

    // Contenido principal de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Encabezado
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                modifier = Modifier
                    .size(32.dp)
                    .clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Resultado de la Búsqueda",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "Filtros",
                tint = Color(0xFF6B3E15),
                modifier = Modifier
                    .size(32.dp)
                    .clickable { showFilters = true }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("Todo", "Popular hoy", "Destacados", "Recomendadas").forEach { filter ->
                Box(
                    modifier = Modifier
                        .background(Color(0xFF6B3E15), MaterialTheme.shapes.small)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable { /* Acción del filtro */ }
                ) {
                    Text(
                        text = filter,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(recipes) { recipe ->
                RecipeItem(recipe = recipe, navController = navController)
            }
        }
    }
}