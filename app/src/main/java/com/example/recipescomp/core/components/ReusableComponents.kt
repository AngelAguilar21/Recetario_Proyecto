package com.example.recipescomp.core.components
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.recipescomp.data.local.entities.FavoriteRecipesEntity
import com.example.recipescomp.core.theme.BrownDark
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun ReusableButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = BrownDark),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
            .width(200.dp) // ancho personalizado
            .height(55.dp) // alto personalizado
    ) {
        Text(text = text, color = Color.White)
    }
}
@Composable
fun ReusableButtonLogin(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = Color(0xFF6B3E15),
    unselectedColor: Color = Color.LightGray,
    selectedTextColor: Color = Color.White,
    unselectedTextColor: Color = Color.Black,
    fontSize: TextUnit = 13.sp
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) selectedColor else unselectedColor,
            contentColor = if (isSelected) selectedTextColor else unselectedTextColor
        ),
        modifier = modifier
    ) {
        Text(text = text, fontSize = fontSize)
    }
}

//CAMPO DE TEXTO PERSONALIZADO
@Composable
fun ReusableLoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}

//TEXTOS
@Composable
fun ReusableText(
    text: String,
    fontSize: TextUnit,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color,
    fontWeight: FontWeight = FontWeight.Normal
){
    Text(
        text = text,
        fontSize = fontSize,
        style = style,
        modifier = modifier,
        color = color,
        fontWeight = fontWeight
    )
}

@Composable
fun BottomNavigationBar(navController: NavController, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavigationItem(
            icon = Icons.Default.Home,
            contentDescription = "Inicio",
            navController = navController,
            route = "Principal"
        )
        BottomNavigationItem(
            icon = Icons.Default.Favorite,
            contentDescription = "Favoritos",
            navController = navController,
            route = "Favoritos"
        )
        BottomNavigationItem(
            icon = Icons.Default.ShoppingCart,
            contentDescription = "Carrito de compras",
            navController = navController,
            route = "listaCompras"
        )
        BottomNavigationItem(
            icon = Icons.Default.Person,
            contentDescription = "Perfil",
            navController = navController,
            route = "perfil"
        )
    }
}
@Composable
fun BottomNavigationItem(
    icon: Any,
    contentDescription: String,
    navController: NavController,
    route: String
) {
    IconButton(
        onClick = {
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            if (currentRoute != route) {
                navController.navigate(route) {
                    popUpTo(route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    ) {
        when (icon) {
            is Painter -> Icon(
                painter = icon,
                contentDescription = contentDescription,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            is ImageVector -> Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }

}

//Boton de regreso
@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onBackground
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Volver atrás",
            tint = tint
        )
    }
}

@Composable
fun FavoriteRecipeCard(
    recipe: FavoriteRecipesEntity,
    onClick: () -> Unit = {},
    onDelete: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(recipe.imageUrl),
            contentDescription = recipe.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = recipe.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Categoría: ${recipe.category ?: "Unknown"}",
                fontSize = 13.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ingredientes: ${recipe.ingredientCount}",
                fontSize = 13.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Origen: ${recipe.area ?: "Unknown"}",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        // Botón para eliminar de favoritos
        IconButton(onClick = {
            onDelete(recipe.name)
        }) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Eliminar de favoritos",
                tint = Color.Red
            )
        }
    }
}

@Composable
fun EditNameDialog(
    initialName: String,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf(initialName) }
    var loading by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                scope.launch {
                    try {
                        loading = true
                        val user = FirebaseAuth.getInstance().currentUser ?: return@launch
                        val userId = user.uid

                        FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(userId)
                            .update("name", name)
                            .await()

                        loading = false
                        onDismiss()
                    } catch (e: Exception) {
                        loading = false
                        Log.e("EditarNombre", "Error: ${e.message}")
                    }
                }
            }) {
                Text(if (loading) "Guardando..." else "Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = { Text("Editar nombre") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nuevo nombre") },
                    singleLine = true
                )
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
