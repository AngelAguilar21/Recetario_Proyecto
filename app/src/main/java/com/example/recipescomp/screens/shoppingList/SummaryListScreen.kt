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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.annotation.RequiresPermission
import android.Manifest
import android.content.ActivityNotFoundException
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Icon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import com.example.recipescomp.R

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
    var debugInfo by remember { mutableStateOf<String>("")}
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    fun parseIngredient(rawIngredient: String): GroupedIngredient? {
        val trimmed = rawIngredient.trim()
        if (trimmed.isEmpty()) return null

        // Lista de unidades de medida comunes que NO son ingredientes
        val measurementUnits = listOf(
            "tsp", "tbsp", "tbs", "cup", "cups", "ml", "l", "liter", "liters",
            "g", "kg", "gram", "grams", "kilogram", "kilograms", "pound", "pounds",
            "oz", "ounce", "ounces", "lb", "lbs", "pint", "pints", "quart", "quarts",
            "gallon", "gallons", "inch", "inches", "cm", "meter", "meters",
            "piece", "pieces", "slice", "slices", "clove", "cloves", "head", "heads",
            "can", "cans", "bottle", "bottles", "pack", "packs", "box", "boxes"
        )

        // Filtrar si es solo una unidad de medida
        if (measurementUnits.any { it.equals(trimmed, ignoreCase = true) }) {
            return null
        }

        // Filtrar texto inválido (solo símbolos, números, etc.)
        val invalidPatterns = listOf(
            Regex("^\\s*[()\\[\\]{}.,;:!?\"'`~@#\$%^&*+=|\\\\/<>_-]*\\s*$"), // Solo símbolos
            Regex("^\\s*\\d+\\s*$"), // Solo números
            Regex("^\\s*[a-zA-Z]\\s*$"), // Solo una letra
            Regex("^\\s*[()]+\\s*$") // Solo paréntesis
        )

        for (pattern in invalidPatterns) {
            if (pattern.matches(trimmed)) {
                return null
            }
        }

        // Limpiar el texto
        val cleaned = trimmed.trim()
        if (cleaned.isEmpty() || cleaned.length < 2) return null

        // Patrones para extraer ingredientes CORRECTAMENTE
        val patterns = listOf(
            // Patrón 1: "Plain Flour (4 cups)" o "Salt (1 tsp)" - CON PARÉNTESIS
            Regex("^(.+?)\\s*\\((\\d+(?:[.,]\\d+)?(?:/\\d+)?)\\s*([a-zA-Z]+)\\)$"),

            // Patrón 2: "Water (2 cups )" - con espacio extra antes del paréntesis
            Regex("^(.+?)\\s*\\((\\d+(?:[.,]\\d+)?(?:/\\d+)?)\\s*([a-zA-Z]+)\\s*\\)$"),

            // Patrón 3: "Black Pepper (1/2 tsp)" - fracciones con paréntesis
            Regex("^(.+?)\\s*\\((\\d+)/(\\d+)\\s*([a-zA-Z]+)\\)$"),

            // Patrón 4: "Lemon (2)" - solo número sin unidad en paréntesis
            Regex("^(.+?)\\s*\\((\\d+(?:[.,]\\d+)?)\\)$"),

            // Patrón 5: "Olive Oil (dash)" o "Mayonnaise (to Serve)" - texto en paréntesis
            Regex("^(.+?)\\s*\\(([^)]+)\\)$"),

            // Patrón 6: "Minced Beef 900g" - sin paréntesis
            Regex("^(.+?)\\s+(\\d+(?:[.,]\\d+)?)\\s*([a-zA-Z]+)$"),

            // Patrón 7: "Allspice 1 tsp" - con espacio entre número y unidad
            Regex("^(.+?)\\s+(\\d+(?:[.,]\\d+)?)\\s+([a-zA-Z]+)$"),

            // Patrón 8: "Black Pepper 1/2 tsp" - fracciones sin paréntesis
            Regex("^(.+?)\\s+(\\d+)/(\\d+)\\s+([a-zA-Z]+)$"),

            // Patrón 9: Solo nombre sin cantidad específica
            Regex("^([^()0-9]+)$")
        )

        for ((index, pattern) in patterns.withIndex()) {
            val match = pattern.find(cleaned)
            match?.let { matchResult ->
                return when (index) {
                    0, 1, 6, 7 -> { // Patrones con cantidad y unidad normales
                        val name = matchResult.groupValues[1].trim()
                        val quantityStr = matchResult.groupValues[2].replace(",", ".")
                        val quantity = quantityStr.toDoubleOrNull() ?: 1.0
                        val unit = if (matchResult.groupValues.size > 3) {
                            matchResult.groupValues[3].trim().lowercase()
                        } else "unit"

                        // Validar que el nombre no sea una unidad de medida
                        if (name.isNotEmpty() &&
                            name.length > 1 &&
                            !measurementUnits.any { it.equals(name, ignoreCase = true) } &&
                            !name.matches(Regex("^\\d+$"))) {
                            GroupedIngredient(name, unit, quantity)
                        } else null
                    }

                    2, 8 -> { // Patrones con fracciones (con y sin paréntesis)
                        val name = matchResult.groupValues[1].trim()
                        val numerator = matchResult.groupValues[2].toDoubleOrNull() ?: 1.0
                        val denominator = matchResult.groupValues[3].toDoubleOrNull() ?: 1.0
                        val quantity = numerator / denominator
                        val unit = matchResult.groupValues[4].trim().lowercase()

                        if (name.isNotEmpty() &&
                            name.length > 1 &&
                            !measurementUnits.any { it.equals(name, ignoreCase = true) } &&
                            !name.matches(Regex("^\\d+$"))) {
                            GroupedIngredient(name, unit, quantity)
                        } else null
                    }

                    3 -> { // "Lemon (2)" - solo número sin unidad
                        val name = matchResult.groupValues[1].trim()
                        val quantity = matchResult.groupValues[2].toDoubleOrNull() ?: 1.0

                        if (name.isNotEmpty() &&
                            name.length > 1 &&
                            !measurementUnits.any { it.equals(name, ignoreCase = true) } &&
                            !name.matches(Regex("^\\d+$"))) {
                            GroupedIngredient(name, "unit", quantity)
                        } else null
                    }

                    4 -> { // "Olive Oil (dash)" - texto descriptivo
                        val name = matchResult.groupValues[1].trim()
                        val descriptor = matchResult.groupValues[2].trim().lowercase()

                        // Manejar descriptores comunes
                        val quantity = when(descriptor) {
                            "dash", "pinch" -> 0.125
                            "to taste", "to serve" -> 1.0
                            else -> 1.0
                        }

                        if (name.isNotEmpty() &&
                            name.length > 1 &&
                            !measurementUnits.any { it.equals(name, ignoreCase = true) } &&
                            !name.matches(Regex("^\\d+$"))) {
                            GroupedIngredient(name, "unit", quantity)
                        } else null
                    }

                    5 -> { // Patrón simple número+unidad
                        val name = matchResult.groupValues[1].trim()
                        val quantityStr = matchResult.groupValues[2].replace(",", ".")
                        val quantity = quantityStr.toDoubleOrNull() ?: 1.0
                        val unit = matchResult.groupValues[3].trim().lowercase()

                        if (name.isNotEmpty() &&
                            name.length > 1 &&
                            !measurementUnits.any { it.equals(name, ignoreCase = true) } &&
                            !name.matches(Regex("^\\d+$"))) {
                            GroupedIngredient(name, unit, quantity)
                        } else null
                    }

                    9 -> { // Solo nombre del ingrediente
                        val name = matchResult.groupValues[1].trim()
                        if (name.isNotEmpty() &&
                            name.length > 2 &&
                            !measurementUnits.any { it.equals(name, ignoreCase = true) } &&
                            !name.matches(Regex("^\\d+$"))) {
                            GroupedIngredient(name, "unit", 1.0)
                        } else null
                    }

                    else -> null
                }
            }
        }

        // Si no coincide con ningún patrón, intentar un parse más simple
        // Buscar el primer número en el string
        val numberPattern = Regex("(\\d+(?:[.,]\\d+)?(?:/\\d+)?)")
        val numberMatch = numberPattern.find(cleaned)

        return if (numberMatch != null) {
            val beforeNumber = cleaned.substring(0, numberMatch.range.first).trim()
            val afterNumber = cleaned.substring(numberMatch.range.last + 1).trim()

            val name = if (beforeNumber.isNotEmpty()) beforeNumber else afterNumber
            val quantity = try {
                if (numberMatch.value.contains("/")) {
                    val parts = numberMatch.value.split("/")
                    parts[0].toDouble() / parts[1].toDouble()
                } else {
                    numberMatch.value.replace(",", ".").toDouble()
                }
            } catch (e: Exception) {
                1.0
            }

            // Buscar unidad después del número
            val unitMatch = Regex("^\\s*([a-zA-Z]+)").find(afterNumber)
            val unit = unitMatch?.groupValues?.get(1)?.lowercase() ?: "unit"

            if (name.isNotEmpty() &&
                name.length > 1 &&
                !measurementUnits.any { it.equals(name, ignoreCase = true) }) {
                GroupedIngredient(name, unit, quantity)
            } else null
        } else {
            // Si no hay números, es solo el nombre del ingrediente
            if (cleaned.length > 2 &&
                !cleaned.matches(Regex("^\\d+$")) &&
                !measurementUnits.any { it.equals(cleaned, ignoreCase = true) }) {
                GroupedIngredient(cleaned, "unit", 1.0)
            } else null
        }
    }

    LaunchedEffect(true) {
        scope.launch {
            try {
                val db = AppDatabase.getInstance(context)
                val items = db.ShoppingListDao().getAllItems(currentUserId)

                val rawIngredients = items.flatMap { item ->
                    // Repite los ingredientes según la cantidad
                    List(item.quantity) {
                        item.ingredients
                            .split(",", ";", "\n") // Dividir por separadores
                            .map { ingredient ->
                                ingredient.trim()
                                    .replace(Regex("\\s+"), " ") // Normalizar espacios múltiples
                            }
                            .filter { ingredient ->
                                ingredient.isNotEmpty() &&
                                        ingredient.length > 2 &&
                                        ingredient.matches(Regex(".*[a-zA-Z].*")) // Debe contener letras
                            }
                    }.flatten()
                }

                debugInfo = "Total raw ingredients: ${rawIngredients.size}\n" +
                        "Raw ingredients: ${rawIngredients.joinToString(", ")}"

                // Parsear cada ingrediente
                val parsedIngredients = rawIngredients.mapNotNull { raw ->
                    parseIngredient(raw)
                }.filter { it.name.isNotEmpty() }

                debugInfo += "\nParsed ingredients: ${parsedIngredients.size}\n" +
                        "Parsed: ${parsedIngredients.map { "${it.name} (${it.quantity} ${it.unit})" }.joinToString(", ")}"

                // Función para normalizar unidades y poder sumarlas correctamente
                fun normalizeUnit(unit: String, quantity: Double): Pair<String, Double> {
                    return when (unit.lowercase()) {
                        "g", "gram", "grams" -> "g" to quantity
                        "kg", "kilogram", "kilograms" -> "g" to (quantity * 1000)
                        "tsp", "teaspoon", "teaspoons" -> "tsp" to quantity
                        "tbsp", "tablespoon", "tablespoons" -> "tsp" to (quantity * 3)
                        "cup", "cups" -> "cup" to quantity
                        "ml", "milliliter", "milliliters" -> "ml" to quantity
                        "l", "liter", "liters" -> "ml" to (quantity * 1000)
                        "oz", "ounce", "ounces" -> "oz" to quantity
                        "lb", "lbs", "pound", "pounds" -> "oz" to (quantity * 16)
                        else -> unit.lowercase() to quantity
                    }
                }

                // Agrupar por nombre de ingrediente y normalizar unidades
                val grouped = mutableMapOf<String, Pair<String, Double>>()

                parsedIngredients.forEach { ingredient ->
                    val normalizedName = ingredient.name.lowercase().trim()
                    val (normalizedUnit, normalizedQuantity) = normalizeUnit(ingredient.unit, ingredient.quantity)

                    if (grouped.containsKey(normalizedName)) {
                        val existing = grouped[normalizedName]!!
                        // Solo sumar si las unidades son compatibles
                        if (existing.first == normalizedUnit) {
                            grouped[normalizedName] = normalizedUnit to (existing.second + normalizedQuantity)
                        } else {
                            // Si las unidades no son compatibles, mantener por separado
                            val newKey = "$normalizedName (${ingredient.unit})"
                            grouped[newKey] = ingredient.unit.lowercase() to ingredient.quantity
                        }
                    } else {
                        grouped[normalizedName] = normalizedUnit to normalizedQuantity
                    }
                }

                // Convertir de vuelta a formato mostrable
                finalList = grouped.map { (name, unitQuantity) ->
                    val (unit, quantity) = unitQuantity
                    val displayName = name.split(" ").joinToString(" ") { word ->
                        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                    }

                    // Convertir unidades grandes de vuelta a formato legible
                    val (displayUnit, displayQuantity) = when (unit) {
                        "g" -> if (quantity >= 1000) "kg" to (quantity / 1000) else "g" to quantity
                        "ml" -> if (quantity >= 1000) "l" to (quantity / 1000) else "ml" to quantity
                        "tsp" -> if (quantity >= 3) "tbsp" to (quantity / 3) else "tsp" to quantity
                        "oz" -> if (quantity >= 16) "lb" to (quantity / 16) else "oz" to quantity
                        else -> unit to quantity
                    }

                    GroupedIngredient(displayName, displayUnit, displayQuantity)
                }.sortedBy { it.name }

                isLoading = false
            } catch (e: Exception) {
                errorMessage = "Error loading ingredients: ${e.message}"
                e.printStackTrace()
                isLoading = false
            }
        }
    }

    // Función para crear el canal de notificación (llamar solo una vez)
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "DOWNLOAD_CHANNEL"
            val name = "Downloads"
            val descriptionText = "Download notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showDownloadNotification(context: Context, fileName: String, fileUri: Uri? = null) {
        createNotificationChannel(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(context, "PDF downloaded to Downloads folder: $fileName", Toast.LENGTH_LONG).show()
                return
            }
        }

        val intent = Intent(Intent.ACTION_VIEW).apply {
            if (fileUri != null) {
                setDataAndType(fileUri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else {
                type = "application/pdf"
            }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, "DOWNLOAD_CHANNEL")
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentTitle("Shopping List Downloaded")
            .setContentText("$fileName saved successfully")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Your shopping list PDF has been saved to Downloads folder: $fileName"))

        try {
            val notificationManager = NotificationManagerCompat.from(context)

            if (notificationManager.areNotificationsEnabled()) {
                notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
            } else {
                Toast.makeText(context, "PDF downloaded to Downloads folder: $fileName", Toast.LENGTH_LONG).show()
            }
        } catch (e: SecurityException) {
            Toast.makeText(context, "PDF downloaded to Downloads folder: $fileName", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    fun exportToPDF(ingredients: List<GroupedIngredient>, forSharing: Boolean = false): Uri? {
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

            // Crear nombre de archivo con timestamp
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "Shopping_List_$timeStamp.pdf"

            var savedUri: Uri? = null
            var success = false

            if (forSharing) {
                // Para compartir: usar directorio de cache
                val cacheDir = context.cacheDir
                val file = File(cacheDir, fileName)

                FileOutputStream(file).use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                    success = true
                }

                if (success) {
                    savedUri = try {
                        FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            file
                        )
                    } catch (e: Exception) {
                        Uri.fromFile(file)
                    }
                }
            } else {
                // Para descargar: usar Downloads
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val resolver = context.contentResolver
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    }

                    savedUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                    savedUri?.let { uri ->
                        resolver.openOutputStream(uri)?.use { outputStream ->
                            pdfDocument.writeTo(outputStream)
                            success = true
                        }
                    }
                } else {
                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val file = File(downloadsDir, fileName)

                    FileOutputStream(file).use { outputStream ->
                        pdfDocument.writeTo(outputStream)
                        success = true
                    }

                    savedUri = Uri.fromFile(file)
                }

                // Mostrar notificación solo para descargas
                if (success) {
                    showDownloadNotification(context, fileName, savedUri)
                    Toast.makeText(context, "PDF downloaded successfully!", Toast.LENGTH_SHORT).show()
                }
            }

            pdfDocument.close()

            if (!success) {
                Toast.makeText(context, "Error: Could not save PDF", Toast.LENGTH_LONG).show()
            }

            return if (success) savedUri else null

        } catch (e: Exception) {
            Toast.makeText(context, "Error creating PDF: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
            return null
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Proceder con la descarga independientemente del resultado
        exportToPDF(finalList)
        if (!isGranted) {
            Toast.makeText(context, "Download completed (notifications disabled)", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para verificar permisos antes de descargar
    fun checkPermissionAndDownload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Solicitar permiso
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                // Permiso ya concedido, proceder con descarga
                exportToPDF(finalList)
            }
        } else {
            // Android < 13, no necesita permiso
            exportToPDF(finalList)
        }
    }

    data class ShareOption(
        val name: String,
        val icon: @Composable () -> Unit,
        val onClick: () -> Unit
    )

    // Dialog de compartir
    fun sharePDFGeneric(packageName: String? = null, appName: String? = null) {
        // Usar la función existente para compartir
        val uri = exportToPDF(finalList, forSharing = true)

        if (uri != null) {
            try {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    putExtra(Intent.EXTRA_SUBJECT, "Shopping List - Ingredients")
                    putExtra(Intent.EXTRA_TEXT, "Here's my shopping list!")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    packageName?.let { setPackage(it) }
                }

                if (packageName != null) {
                    context.startActivity(intent)
                } else {
                    context.startActivity(Intent.createChooser(intent, "Share Shopping List PDF"))
                }

                showDialog = false
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    appName?.let { "$it no está instalado" } ?: "No app found to share PDF",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Simplificar todas las funciones de compartir:
    fun shareViaWhatsApp() {
        sharePDFGeneric("com.whatsapp", "WhatsApp")
    }

    fun shareViaEmail() {
        val uri = exportToPDF(finalList, forSharing = true)
        if (uri != null) {
            try {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    putExtra(Intent.EXTRA_SUBJECT, "Shopping List - Ingredients")
                    putExtra(Intent.EXTRA_TEXT, "Adjunto mi lista de compras.")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(intent, "Enviar email..."))
                showDialog = false
            } catch (e: Exception) {
                Toast.makeText(context, "Error al compartir por email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun shareViaFacebook() {
        sharePDFGeneric("com.facebook.katana", "Facebook")
    }

    fun shareViaInstagram() {
        val uri = exportToPDF(finalList, forSharing = true)
        if (uri != null) {
            try {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    setPackage("com.instagram.android")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(intent)
                showDialog = false
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    "Instagram no puede compartir PDFs directamente",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun shareViaX() {
        val uri = exportToPDF(finalList, forSharing = true)
        if (uri != null) {
            try {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    putExtra(Intent.EXTRA_TEXT, "Mi lista de compras 🛒")
                    setPackage("com.twitter.android")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(intent)
                showDialog = false
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    "X no puede compartir PDFs. Descarga el archivo primero.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

// DIALOG COMPLETO SIMPLIFICADO
    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Título con Material 3
                    Text(
                        text = "Compartir Lista de Compras",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        textAlign = TextAlign.Center
                    )

                    // Lista de opciones de compartir con imágenes
                    val shareOptions = listOf(
                        ShareOption(
                            name = "Descargar",
                            icon = {
                                Surface(
                                    modifier = Modifier.size(56.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    border = BorderStroke(
                                        width = 2.dp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Download,
                                            contentDescription = "Download",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                }
                            },
                            onClick = {
                                checkPermissionAndDownload()
                                showDialog = false
                            }
                        ),
                        ShareOption(
                            name = "Facebook",
                            icon = {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_facebook),
                                    contentDescription = "Facebook",
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            },
                            onClick = { shareViaFacebook() }
                        ),
                        ShareOption(
                            name = "WhatsApp",
                            icon = {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_whatsapp),
                                    contentDescription = "WhatsApp",
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            },
                            onClick = { shareViaWhatsApp() }
                        ),
                        ShareOption(
                            name = "Gmail",
                            icon = {
                                Surface(
                                    modifier = Modifier.size(56.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outlineVariant
                                    )
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_gmail),
                                        contentDescription = "Gmail",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(8.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                            },
                            onClick = { shareViaEmail() }
                        ),
                        ShareOption(
                            name = "X",
                            icon = {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_x_twitter),
                                    contentDescription = "X",
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            },
                            onClick = { shareViaX() }
                        ),
                        ShareOption(
                            name = "Instagram",
                            icon = {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_instagram),
                                    contentDescription = "Instagram",
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            },
                            onClick = { shareViaInstagram() }
                        )
                    )

                    // Grid de opciones con scroll horizontal
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .wrapContentWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            shareOptions.forEach { option ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .width(72.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { option.onClick() }
                                        .padding(4.dp)
                                ) {
                                    option.icon()
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = option.name,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón de cancelar con Material 3
                    FilledTonalButton(
                        onClick = { showDialog = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Text(
                            text = "Cancelar",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
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

                            // Mostrar información de debug si está disponible
                            if (debugInfo.isNotEmpty()) {
                                Text(
                                    debugInfo,
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }

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

                            if (debugInfo.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Debug Info:",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    debugInfo,
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
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