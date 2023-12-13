package com.said.chat.sceens

import android.window.SplashScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.said.chat.navigation.Screens
import kotlinx.coroutines.delay

@Preview
@Composable
fun showSplash(){
    SplashScreen(navController = rememberNavController())

    }



@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit){
        delay(3000)
        navController.navigate(Screens.Home.route)
    }}