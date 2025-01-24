package com.example.visaapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.navigation.NavController

@Composable
fun ProfileScreen(navController: NavController) {
    // Sample User Data
    val name = "John Doe"
    val email = "john.doe@example.com"
    val passportNumber = "A12345678"
    val phoneNumber = "+1234567890"
    val dateOfBirth = "01/01/1990"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Name: $name", style = MaterialTheme.typography.titleLarge)
        Text(text = "Email: $email", style = MaterialTheme.typography.titleLarge)
        Text(text = "Passport Number: $passportNumber", style = MaterialTheme.typography.titleLarge)
        Text(text = "Phone Number: $phoneNumber", style = MaterialTheme.typography.titleLarge)
        Text(text = "Date of Birth: $dateOfBirth", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("login") }) {
            Text("Sign Out")
        }
    }
}
