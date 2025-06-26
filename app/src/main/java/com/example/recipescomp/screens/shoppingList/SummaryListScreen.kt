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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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

// 📦 Structured data class for a final grouped ingredient
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

    LaunchedEffect(true) {
        scope.launch {
            val db = AppDatabase.getInstance(context)
            val items = db.ShoppingListDao().getAllItems()

            val grouped = mutableMapOf<Pair<String, String>, Double>()
            items.flatMap { it.ingredients.split(",") }
                .mapNotNull { raw ->
                    val match = Regex("(.+?)\\((\\d+(?:[.,]?\\d*)?)\\s*(\\w+)\\)").find(raw.trim())
                    match?.let {
                        val name = it.groupValues[1].trim().lowercase()
                        val quantity = it.groupValues[2].replace(",", ".").toDoubleOrNull() ?: 0.0
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
            }
        }
    }

    fun exportToPDF(ingredients: List<GroupedIngredient>) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = android.graphics.Paint()

        var y = 25
        paint.textSize = 12f
        canvas.drawText("Ingredients List", 10f, y.toFloat(), paint)
        y += 20

        ingredients.forEachIndexed { index, ingredient ->
            val line = "${index + 1}. ${ingredient.quantity} ${ingredient.unit} ${ingredient.name.capitalize()}"
            canvas.drawText(line, 10f, y.toFloat(), paint)
            y += 18
        }

        pdfDocument.finishPage(page)

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "Ingredients_List_$timeStamp.pdf"
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(directory, fileName)

        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(context, "PDF saved at: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Error saving PDF", Toast.LENGTH_LONG).show()
        } finally {
            pdfDocument.close()
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    "Share List",
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
            // 🧭 Top bar with back button and title
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
                        text = "Ingredients List",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (finalList.isEmpty()) {
                // Message if no ingredients
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No ingredients to display.",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            } else {
                // Section title
                Text(
                    text = "Final List:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = BrownDark
                )

                // Ingredients list
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
                                // Ingredient number
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

                                // Ingredient information
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = ingredient.name.replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                                        },
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = BrownDark
                                    )
                                    Text(
                                        text = "${ingredient.quantity} ${ingredient.unit}",
                                        fontSize = 14.sp,
                                        color = Color.DarkGray
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 📤 Share button with added padding
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
                        "Share List",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }

        // 🔽 BOTTOM NAVIGATION BAR
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