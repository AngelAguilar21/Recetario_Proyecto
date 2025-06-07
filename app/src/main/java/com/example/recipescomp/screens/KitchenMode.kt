package com.example.recipescomp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipescomp.R
import com.example.recipescomp.ui.theme.BrownDark

@Composable
fun Modo_Cocina(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
            .background(Color(0xFFF7F2E7)),
            verticalArrangement = Arrangement.SpaceBetween
    ) {
        // ENCABEZADO
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(BrownDark)
                .padding(horizontal = 16.dp)
                .padding(top = 40.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Ícono decorativo
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.cat_icon), // Reemplazar con el ícono correspondiente
                        contentDescription = "Ícono de Modo Cocina",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = "Modo Cocina",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }

                // Botón de salir
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Transparent) // Fondo transparente
                        .clickable {
                            navController.popBackStack() // Acción para salir
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.exit_icon), // Reemplazar con el ícono correcto
                        contentDescription = "Salir",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    )
                }
            }
        }

        // SECCIÓN DE INFORMACIÓN DE LA RECETA
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título de la receta
            Text(
                text = "Chaufa", // Cambiado a "Chaufa"
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = BrownDark,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Etiquetas de categoría y dificultad
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Plato Fuerte", // Etiqueta de categoría
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .background(Color(0xFFE1B38B), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
                Text(
                    text = "Intermedio", // Etiqueta de dificultad
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .background(Color(0xFF76C05A), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Imagen decorativa cuadrada
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(BrownDark, RoundedCornerShape(16.dp))
            )
        }

        // IMAGEN CENTRAL DEL PASO
        Image(
            painter = painterResource(id = R.drawable.sarten_icon), // Reemplazar con una imagen representativa del paso
            contentDescription = "Paso actual",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 16.dp)
        )

        // TEXTO DEL PASO ACTUAL
        Text(
            text = "Poner a calentar 2 cucharadas de aceite en una sartén grande.", // Primer paso dinámico
            fontSize = 18.sp,
            color = BrownDark,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // INDICADORES DE PROGRESO + BOTONES DE NAVEGACIÓN
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Indicadores de progreso
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                color = if (index == 0) BrownDark else Color.Gray,
                                shape = CircleShape
                            )
                            .padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones de navegación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* Acción para ir al paso anterior */ },
                    shape = RoundedCornerShape(8.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = BrownDark
                    )
                ) {
                    Text(text = "Atrás", fontSize = 16.sp, color = Color.White)
                }

                Button(
                    onClick = { /* Acción para ir al siguiente paso */ },
                    shape = RoundedCornerShape(8.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = BrownDark
                    )
                ) {
                    Text(text = "Continuar", fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}