//package com.example.visaapplication.ui.screens
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import androidx.compose.ui.Alignment
//
//@Composable
//fun HomeScreen(navController: NavController) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Button(onClick = { navController.navigate("apply") }) {
//            Text("Apply Now")
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = { navController.navigate("track") }) {
//            Text("Track Your Application")
//        }
//    }
//}

package com.example.visaapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.visaapplication.R

@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.bg_photo), // Replace with your background image
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop, // Scale the image to fill the screen
            alpha = 0.7f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Greeting Section
            Spacer(modifier = Modifier.height(64.dp)) // Move text lower
            GreetingText()

            Spacer(modifier = Modifier.weight(1f)) // Push buttons to the center
            // Buttons
            Button(onClick = { navController.navigate("apply") }) {
                Text("Apply Now")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("track") }) {
                Text("Track Your Application")
            }
            Spacer(modifier = Modifier.weight(1f)) // Push buttons to the middle vertically
        }
    }
}

@Composable
fun GreetingText() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Welcome Back, User!",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary // Adjust text color for better contrast
            ),
            textAlign = TextAlign.Center
        )
        Text(
            text = "What would you like to do today?",
            style = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary
            ),
            textAlign = TextAlign.Center
        )
    }
}
