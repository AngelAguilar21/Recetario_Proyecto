package com.example.recipescomp.screens.shoppingList

import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipescomp.components.BackButton
import com.example.recipescomp.components.BottomNavigationBar
import com.example.recipescomp.data.local.AppDatabase
import com.example.recipescomp.ui.theme.BrownDark
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max

data class GroupedIngredient(
    val name: String,
    val unit: String,
    val quantity: Double
)

@Composable
fun SummaryListScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var finalList by remember { mutableStateOf<List<GroupedIngredient>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(true) {
        scope.launch {
            try {
                val db = AppDatabase.getInstance(context)
                val items = db.ShoppingListDao().getAllItems()

                val grouped = mutableMapOf<Pair<String, String>, Double>()
                items.flatMap { it.ingredients.split(",") }
                    .mapNotNull { raw ->
                        // Regex mejorado para mejor parsing
                        val match = Regex("(.+?)\\((\\d+(?:[.,]\\d+)?)\\s*(\\w+)\\)").find(raw.trim())
                        match?.let {
                            val name = it.groupValues[1].trim().lowercase()
                            // Manejo más robusto de números decimales
                            val quantityStr = it.groupValues[2].replace(",", ".")
                            val quantity = quantityStr.toDoubleOrNull() ?: 0.0
                            val unit = it.groupValues[3].trim().lowercase()
                            Triple(name, unit, quantity)
                        }
                    }
                    .forEach { (name, unit, quantity) ->
                        val key = name to unit
                        grouped[key] = grouped.getOrDefault(key, 0.0) + quantity
                    }

                finalList = grouped.map { (key, quantity) ->
                    GroupedIngredient(name = key.first, unit = key.second, quantity = quantity)
                }.sortedBy { it.name }

                isLoading = false
            } catch (e: Exception) {
                errorMessage = "Error loading ingredients: ${e.message}"
                isLoading = false
            }
        }
    }

    fun exportToPDF(ingredients: List<GroupedIngredient>) {
        try {
            val pdfDocument = PdfDocument()

            // Calcular altura dinámica basada en número de ingredientes
            val baseHeight = 100
            val itemHeight = 25
            val totalHeight = max(600, baseHeight + (ingredients.size * itemHeight))

            val pageInfo = PdfDocument.PageInfo.Builder(400, totalHeight, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            val paint = android.graphics.Paint()

            var y = 40

            // Título
            paint.textSize = 16f
            paint.isFakeBoldText = true
            canvas.drawText("Shopping List - Ingredients", 20f, y.toFloat(), paint)
            y += 30

            // Línea separadora
            paint.strokeWidth = 2f
            canvas.drawLine(20f, y.toFloat(), 380f, y.toFloat(), paint)
            y += 25

            // Lista de ingredientes
            paint.textSize = 12f
            paint.isFakeBoldText = false

            ingredients.forEachIndexed { index, ingredient ->
                val formattedQuantity = if (ingredient.quantity % 1 == 0.0) {
                    ingredient.quantity.toInt().toString()
                } else {
                    String.format("%.2f", ingredient.quantity)
                }

                val capitalizedName = ingredient.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }

                val line = "${index + 1}. $formattedQuantity ${ingredient.unit} $capitalizedName"
                canvas.drawText(line, 20f, y.toFloat(), paint)
                y += 20
            }

            pdfDocument.finishPage(page)

            // Guardar en directorio interno de la app para evitar problemas de permisos
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "Shopping_List_$timeStamp.pdf"
            val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val file = File(directory, fileName)

            pdfDocument.writeTo(FileOutputStream(file))
            pdfDocument.close()

            Toast.makeText(
                context,
                "PDF saved successfully!\nLocation: ${file.absolutePath}",
                Toast.LENGTH_LONG
            ).show()

        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Error creating PDF: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Dialog de compartir
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    "Share Shopping List",
                    color = BrownDark,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        "How would you like to share your ingredients list?",
                        color = Color.DarkGray
                    )
                    Button(
                        onClick = {
                            exportToPDF(finalList)
                            showDialog = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .shadow(4.dp, RoundedCornerShape(12.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = BrownDark),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Download PDF",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text(
                        "Cancel",
                        color = BrownDark,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

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
            // Top bar con botón back y título
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
                        text = "Shopping List",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> {
                    // Indicador de carga
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = BrownDark
                        )
                    }
                }

                errorMessage != null -> {
                    // Mensaje de error
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                errorMessage!!,
                                color = Color.Red,
                                fontSize = 16.sp
                            )
                            Button(
                                onClick = {
                                    errorMessage = null
                                    isLoading = true
                                    // Reintentar carga
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BrownDark)
                            ) {
                                Text("Retry", color = Color.White)
                            }
                        }
                    }
                }

                finalList.isEmpty() -> {
                    // Lista vacía
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "No ingredients found",
                                color = Color.Gray,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "Add some recipes to generate your shopping list",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                else -> {
                    // Lista de ingredientes
                    Text(
                        text = "Final Shopping List (${finalList.size} items):",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = BrownDark
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.8f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(finalList) { index, ingredient ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(2.dp, RoundedCornerShape(12.dp)),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F2E7))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Número del ingrediente
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(
                                                BrownDark,
                                                RoundedCornerShape(8.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${index + 1}",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    // Información del ingrediente
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = ingredient.name.replaceFirstChar {
                                                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                                            },
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = BrownDark
                                        )

                                        val formattedQuantity = if (ingredient.quantity % 1 == 0.0) {
                                            ingredient.quantity.toInt().toString()
                                        } else {
                                            String.format("%.2f", ingredient.quantity)
                                        }

                                        Text(
                                            text = "$formattedQuantity ${ingredient.unit}",
                                            fontSize = 14.sp,
                                            color = Color.DarkGray
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón de compartir
                    Button(
                        onClick = { showDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .height(56.dp)
                            .shadow(8.dp, RoundedCornerShape(16.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = BrownDark),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            "Share Shopping List",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Barra de navegación inferior
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

