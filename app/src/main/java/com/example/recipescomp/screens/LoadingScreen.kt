package com.example.recipescomp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recipescomp.R


@Composable
fun Inicio_Login(navController: NavController){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())

    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_principal),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.banner),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 16.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    navController.navigate("Login_Principal")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B3E15))
            ) {
                Text(text = "INGRESAR", color = Color.White)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "¿Eres nuevo? Regístrate",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                navController.navigate("Registrarse")
                }
            )
        }
    }
}