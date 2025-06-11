package com.example.recipescomp.screens.home


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipescomp.ui.theme.BrownDark

@Composable
fun HeaderSection(navController: NavController) {
    var busqueda by remember { mutableStateOf("") }
    // 🟫 ENCABEZADO PRINCIPAL - Parte marrón superior

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(
                BrownDark,
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding() // ✅ Corregido para evitar que tape el status bar
                .padding(16.dp)
        ) {
            // 👤 Bienvenida + ícono de perfil
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Text(
                        text = "WELCOME CHEF",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "The ingredients await you...",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Usuario",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                navController.navigate("Perfil")
                            }
                    )
                }
            }

            // 🔍 Barra de búsqueda
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
                    .clickable {
                    // Al hacer clic, navegamos a la pantalla de búsqueda
                    navController.navigate("search")
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Ícono de búsqueda",
                    tint = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Buscar recetas...",
                    color = Color.Gray,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }

}
