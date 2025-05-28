package com.example.recipescomp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.FlowRow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterSheetContent(
    onApplyFilters: () -> Unit,
    onClearFilters: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Filtros: Momento del día
        Text(
            text = "Momento del día",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF6B3E15)
        )
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("Desayuno", "Almuerzo", "Cena", "Media mañana/Media tarde").forEach { option ->
                FilterOption(option)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filtros: Tipo de Plato
        Text(
            text = "Tipo de plato",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF6B3E15)
        )
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("Desayunos", "Postres", "Almuerzos", "Bebidas", "Saludables", "Express").forEach { option ->
                FilterOption(option)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filtros: Dificultad
        Text(
            text = "Dificultad",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF6B3E15)
        )
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("Fácil", "Media", "Difícil").forEach { option ->
                FilterOption(option)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de Acción
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onClearFilters,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B3E15))
            ) {
                Text(text = "Limpiar Filtros", color = Color.White)
            }
            Button(
                onClick = onApplyFilters,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B3E15))
            ) {
                Text(text = "Hecho", color = Color.White)
            }
        }
    }
}

@Composable
fun FilterOption(option: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFEFEFEF), RoundedCornerShape(16.dp)) // Fondo claro con esquinas redondeadas
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { /* Acción cuando se selecciona una opción */ }
    ) {
        Text(
            text = option,
            fontSize = 12.sp, // Tamaño reducido para evitar que el texto se desborde
            color = Color(0xFF6B3E15) // Color marrón para mantener el tema
        )
    }
}
