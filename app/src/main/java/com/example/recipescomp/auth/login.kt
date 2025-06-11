package com.example.recipescomp.auth

import com.example.recipescomp.components.ReusableButton
import com.example.recipescomp.components.ReusableButtonLogin
import com.example.recipescomp.components.ReusableLoginTextField
import com.example.recipescomp.components.ReusableText
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun Login_Principal(navController: NavController){
    var isLogin by remember { mutableStateOf(true) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_login_2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(horizontal = 40.dp)
                .align(Alignment.TopCenter)
                .offset(y = 250.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ReusableButtonLogin(
                        text = "LOGIN",
                        isSelected = isLogin,
                        onClick = { isLogin = true },
                        modifier = Modifier.weight(1f),
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    ReusableButtonLogin(
                        text = "REGISTER",
                        isSelected = !isLogin,
                        onClick = { navController.navigate("Registrarse") },
                        modifier = Modifier.weight(1f),
                        selectedColor = Color.LightGray,
                        unselectedColor = Color.LightGray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Black,
                        fontSize = 10.sp
                    )
                }
                ReusableText("WELCOME CHEF",30.sp, MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 30.dp), color=Color.Black)

                Spacer(modifier = Modifier.height(12.dp))

                ReusableLoginTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = "User name",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                ReusableLoginTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    modifier = Modifier.fillMaxWidth(),
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(30.dp))

                ReusableButton("LOGIN", onClick = {
                    navController.navigate("Principal")
                })
            }
        }
    }
}