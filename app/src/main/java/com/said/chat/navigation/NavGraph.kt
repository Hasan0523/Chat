package com.said.chat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.said.chat.sceens.ChangePasswordScreen
import com.said.chat.sceens.ChatScreen
import com.said.chat.sceens.HomeScreen
import com.said.chat.sceens.LoginScreen
import com.said.chat.sceens.RegisterScreen
import com.said.chat.sceens.SplashScreen

@Composable
fun NavGraph (){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.Splash.route)
    {
        composable(route = Screens.Splash.route){
            SplashScreen(navController)
        }
        composable(route = Screens.Home.route) {
            HomeScreen(navController)
        }
        composable(route = Screens.Regist.route) {
            RegisterScreen(navController)
        }
        composable(route = Screens.Login.route) {
            LoginScreen(navController)
        }
        composable(route = Screens.Chat.route, arguments = listOf(
            navArgument("key"){type= NavType.StringType}
        )) {
            ChatScreen(navController, it.arguments?.getString("key")!!)
        }
        composable(Screens.ChangePassword.route){
            ChangePasswordScreen()
        }
    }
}