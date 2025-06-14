package com.example.recipescomp.screens.shoppingList

import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipescomp.data.local.AppDatabase
import com.example.recipescomp.ui.theme.BrownDark
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

// 📦 Data class estructurada para un ingrediente final agrupado
data class IngredienteAgrupado(
    val nombre: String,
    val unidad: String,
    val cantidad: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryListScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var listaFinal by remember { mutableStateOf<List<IngredienteAgrupado>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        scope.launch {
            val db = AppDatabase.getInstance(context)
            val items = db.ShoppingListDao().getAllItems()

            val agrupados = mutableMapOf<Pair<String, String>, Double>()
            items.flatMap { it.ingredients.split(",") }
                .mapNotNull { raw ->
                    val match = Regex("(.+?)\\((\\d+(?:[.,]?\\d*)?)\\s*(\\w+)\\)").find(raw.trim())
                    match?.let {
                        val nombre = it.groupValues[1].trim().lowercase()
                        val cantidad = it.groupValues[2].replace(",", ".").toDoubleOrNull() ?: 0.0
                        val unidad = it.groupValues[3].trim().lowercase()
                        Triple(nombre, unidad, cantidad)
                    }
                }
                .forEach { (nombre, unidad, cantidad) ->
                    val key = nombre to unidad
                    agrupados[key] = agrupados.getOrDefault(key, 0.0) + cantidad
                }

            listaFinal = agrupados.map { (key, cantidad) ->
                IngredienteAgrupado(nombre = key.first, unidad = key.second, cantidad = cantidad)
            }
        }
    }

    fun exportToPDF(ingredientes: List<IngredienteAgrupado>) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = android.graphics.Paint()

        var y = 25
        paint.textSize = 12f
        canvas.drawText("Lista de Ingredientes", 10f, y.toFloat(), paint)
        y += 20

        ingredientes.forEachIndexed { index, ingrediente ->
            val line = "${index + 1}. ${ingrediente.cantidad} ${ingrediente.unidad} ${ingrediente.nombre.capitalize()}"
            canvas.drawText(line, 10f, y.toFloat(), paint)
            y += 18
        }

        pdfDocument.finishPage(page)

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "Lista_Ingredientes_$timeStamp.pdf"
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(directory, fileName)

        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(context, "PDF guardado en: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error al guardar PDF", Toast.LENGTH_LONG).show()
        } finally {
            pdfDocument.close()
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Compartir", color = BrownDark) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = {
                            exportToPDF(listaFinal)
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrownDark)
                    ) {
                        Text("Descargar", color = Color.White)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar", color = BrownDark)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Ingredientes", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BrownDark)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Lista:",
                fontSize = 20.sp,
                color = BrownDark,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(listaFinal) { index, ingrediente ->
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = BrownDark)) {
                                append("${index + 1}. ")
                            }
                            append("${ingrediente.cantidad} ${ingrediente.unidad} ${ingrediente.nombre.capitalize()}")
                        },
                        fontSize = 16.sp
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    // 📤 Botón que abre el diálogo para compartir
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = { showDialog = true },
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrownDark)
                        ) {
                            Text("Compartir", color = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}






