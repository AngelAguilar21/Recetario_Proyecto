package com.example.recipescomp.screens.shoppingList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipescomp.R
import com.example.recipescomp.shared.ListaDeComprasState
import com.example.recipescomp.shared.Receta
import com.example.recipescomp.ui.theme.BrownDark

@Composable
fun Lista_Compras(navController: NavController) {
    val recetas = remember { ListaDeComprasState.recetasSeleccionadas }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Lista de Compras",
            fontSize = 30.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(BrownDark)
                .padding(16.dp),
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (recetas.isEmpty()) {
            Text("NO SE HA SELECCIONADO NINGUNA RECETA", fontSize = 20.sp, color = Color.Gray)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                recetas.forEach { receta ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(Color(0xFFF7F2E7), RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(Color.Gray, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No Image", color = Color.White, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = receta.nombre,
                            fontSize = 18.sp,
                            color = BrownDark,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = { ListaDeComprasState.recetasSeleccionadas.remove(receta) }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_delete),
                                contentDescription = "Eliminar",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("lista_ingredientes") },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrownDark)
        ) {
            Text("Generar Lista", fontSize = 18.sp, color = Color.White)
        }
    }
}


