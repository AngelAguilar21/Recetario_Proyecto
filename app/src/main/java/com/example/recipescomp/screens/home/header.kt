package com.example.recipescomp.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
fun header(navController: NavController){

    var busqueda by remember { mutableStateOf("") }

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
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            // 👤 Bienvenida + ícono de perfil
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Bienvenido",
                        color = Color.White,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "los ingredientes te esperan...",
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

            // 🔍 Barra de búsqueda + botón de filtro
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    placeholder = { Text("Buscar", color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Ícono de búsqueda",
                            tint = Color.Gray
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(2f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        // Acción al hacer clic
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.White, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatListNumbered,
                        contentDescription = "Ícono de búsqueda",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}