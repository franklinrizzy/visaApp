package com.example.visaapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.visaapplication.ui.screens.*
import com.example.visaapplication.ui.payment.*

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("apply/{selectedCountry}") { backStackEntry ->
            val selectedCountry = backStackEntry.arguments?.getString("selectedCountry") ?: "Unknown"
            ApplyScreen(navController, selectedCountry)
        }
        composable("track") { TrackScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("edit_profile") { EditProfileScreen(navController) }
        composable("upload_documents") { UploadDocumentsScreen(navController) }
        composable("dummy_payment") { DummyPaymentScreen(navController) }

    }
}
