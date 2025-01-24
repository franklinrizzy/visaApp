package com.example.visaapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.visaapplication.ui.screens.*

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("apply") { ApplyScreen(navController) }
        composable("track") { TrackScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
//        composable("payment") { PaymentScreen() }
    }
}
