package com.said.chat.sceens

import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.said.chat.R
import com.said.chat.api.SharedHelper
import com.said.chat.navigation.Screens
import kotlinx.coroutines.delay
@Composable

    fun SplashScreen(navController: NavHostController) {
    val context = LocalContext.current
        LaunchedEffect(Unit){
            delay(3000)
            if (SharedHelper.getInstance(context).getKey().isEmpty()) navController.navigate(Screens.Login.route)
            else navController.navigate(Screens.Home.route)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.background)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                modifier = Modifier.size(250.dp),
                painter = painterResource(id = R.drawable.splash_img),
                contentDescription ="splash",
            )

        }
    }