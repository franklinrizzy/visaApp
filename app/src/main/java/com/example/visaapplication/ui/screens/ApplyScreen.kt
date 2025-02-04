package com.example.visaapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ApplyScreen(navController: NavController) {
    var country by remember { mutableStateOf("") }
    var passportNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var additionalInfo by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = country, onValueChange = { country = it }, label = { Text("Country") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = passportNumber, onValueChange = { passportNumber = it }, label = { Text("Passport Number") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = address, onValueChange = { address = it }, label = { Text("Address") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = additionalInfo, onValueChange = { additionalInfo = it }, label = { Text("Additional Information") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // Submit Application Logic Here
            navController.navigate("home")
        }) {
            Text("Submit Application")
        }
    }
}