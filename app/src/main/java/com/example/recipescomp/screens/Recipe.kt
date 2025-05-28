package com.example.recipescomp.screens

import BackButton
import BottomNavigationBar
import ReusableButton
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recipescomp.ui.theme.BrownDark

@Composable
fun Receta(navController: NavController) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("INGREDIENTES", "PASO A PASO")
    var portions by remember { mutableStateOf(1) }

    Box(modifier = Modifier.fillMaxSize().padding(top = 10.dp)){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp)
                .padding(horizontal = 10.dp, vertical = 16.dp)
        ) {
            BackButton(onClick = { navController.popBackStack() })

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "NOMBRE DE LA RECETA",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Descripción...",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.padding(horizontal = 16.dp),
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
            // CONTENIDO SCROLLABLE
            when (selectedTab) {
                0 -> {
                    val ingredientes = listOf(
                        "Ingrediente 1" to "Cantidad y Medida",
                        "Ingrediente 2" to "Cantidad y Medida",
                        "Ingrediente 3" to "Cantidad y Medida",
                        "Ingrediente 4" to "Cantidad y Medida",
                        "Ingrediente 5" to "Cantidad y Medida",
                        "Ingrediente 6" to "Cantidad y Medida",
                        "Ingrediente 7" to "Cantidad y Medida",
                        "Ingrediente 8" to "Cantidad y Medida",
                        "Ingrediente 9" to "Cantidad y Medida"
                    )

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = "$portions PORCIONES",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

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
                }

                1 -> {
                    val pasos = listOf("Paso 1", "Paso 2", "Paso 3", "Paso 4", "Paso 5", "Paso 6", "Paso 7", "Paso 8", "Paso 9", "Paso 10")

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        itemsIndexed(pasos) { index, step ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(BrownDark, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "${index + 1}",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(step)
                            }
                        }
                    }
                }
            }
            ReusableButton(
                "Modo Cocina",
                onClick = { navController.navigate("modoCocina") },
                modifier = Modifier.padding(horizontal = 30.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
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