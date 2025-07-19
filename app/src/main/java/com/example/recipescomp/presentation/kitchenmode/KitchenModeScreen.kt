package com.example.recipescomp.presentation.kitchenmode

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.recipescomp.viewmodel.MealViewModel
import com.example.recipescomp.core.theme.BrownDark
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import com.example.recipescomp.R
import com.airbnb.lottie.compose.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Modo_Cocina(
    navController: NavController,
    mealId: String,
    viewModel: MealViewModel = viewModel()
) {
    val meal by viewModel.selectedMeal

    LaunchedEffect(mealId) {
        viewModel.fetchMealById(mealId)
    }

    if (meal == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading recipe...")
        }
        return
    }

    val pasos = meal?.strInstructions
        ?.split("\n")
        ?.filter { it.isNotBlank() }
        ?: listOf("No instructions available.")

    val pagerState = rememberPagerState(initialPage = 0)
    val scope = rememberCoroutineScope()

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cat_cook))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                        contentDescription = "Go out",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    )
                }
            }
        }

        // 🔲 Descripcion
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFECE0D1))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = meal!!.strMealThumb,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.LightGray, RoundedCornerShape(16.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = meal!!.strMeal,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrownDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = meal!!.strCategory ?: "Uncategorized",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color(0xFFFFC107), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = meal!!.strArea ?: "Without country",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color(0xFF4CAF50), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // 📄 PANTALLAS DESLIZABLES (Pasos)
        HorizontalPager(
            count = pasos.size,
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
            userScrollEnabled = false
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.SpaceBetween, // 👈 Esto reparte arriba y abajo
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imagen animada
                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                )


                // 📄 Paso como LazyColumn (solo el texto puede hacer scroll)
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        AnimatedContent(
                            targetState = pasos[page],
                            transitionSpec = {
                                slideInHorizontally { it } + fadeIn() with
                                        slideOutHorizontally { -it } + fadeOut()
                            },
                            label = "PasoAnimado"
                        ) { paso ->
                            Text(
                                text = paso,
                                fontSize = 18.sp,
                                color = BrownDark,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                }

                // Parte inferior fija: Bolitas + Botones
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 🔘 Indicador de progreso
                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        activeColor = BrownDark,
                        inactiveColor = Color.Gray,
                        modifier = Modifier.padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (pagerState.currentPage == pasos.lastIndex) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (pagerState.currentPage > 0) {
                                Button(
                                    onClick = {
                                        scope.launch {
                                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                        }
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = BrownDark)
                                ) {
                                    Text("Anterior", color = Color.White, fontSize = 16.sp)
                                }
                            } else {
                                Spacer(modifier = Modifier.width(1.dp))
                            }

                            Button(
                                onClick = {
                                    navController.popBackStack()
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BrownDark)
                            ) {
                                Text("Finish", color = Color.White, fontSize = 16.sp)
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (pagerState.currentPage > 0) {
                                Button(
                                    onClick = {
                                        scope.launch {
                                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                        }
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = BrownDark)
                                ) {
                                    Text("Anterior", color = Color.White, fontSize = 16.sp)
                                }
                            } else {
                                Spacer(modifier = Modifier.width(1.dp))
                            }

                            Button(
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BrownDark)
                            ) {
                                Text("Next", color = Color.White, fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
        }

    }
}



