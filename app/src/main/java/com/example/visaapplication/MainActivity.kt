//package com.example.visaapplication
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.compose.rememberNavController
//import com.example.visaapplication.ui.navigation.AppNavGraph
//import com.example.visaapplication.ui.components.BottomNavBar
//import com.example.visaapplication.ui.theme.VisaApplicationTheme
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            VisaApplicationTheme {
//                val navController = rememberNavController()
//                MainScreen(navController)
//            }
//        }
//    }
//}
//
//@Composable
//fun MainScreen(navController: androidx.navigation.NavHostController) {
//    val currentBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = currentBackStackEntry?.destination?.route
//
//    Column(Modifier.fillMaxSize()) {
//        Box(Modifier.weight(1f)) {
//            AppNavGraph(navController)
//        }
//
//        // Show BottomNavBar only if not on Login or Signup routes
//        if (currentRoute != "login" && currentRoute != "signup") {
//            BottomNavBar(navController)
//        }
//    }
//}

package com.example.visaapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Airlines
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.visaapplication.ui.navigation.AppNavGraph
import com.example.visaapplication.ui.components.BottomNavBar
import com.example.visaapplication.ui.theme.VisaApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VisaApplicationTheme {
                val navController = rememberNavController()
                MainScreen(navController)
            }
        }
    }
}

@Composable
fun MainScreen(navController: androidx.navigation.NavHostController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Column(Modifier.fillMaxSize()) {
        // Add TopAppBar
        TopAppBarWithLogoAndTitle()

        Box(Modifier.weight(1f)) {
            AppNavGraph(navController)
        }

        // Show BottomNavBar only if not on Login or Signup routes
        if (currentRoute != "login" && currentRoute != "signup") {
            BottomNavBar(navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithLogoAndTitle() {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Add Logo
                Icon(
                    imageVector = Icons.Default.Airlines, // Use Material Design icon
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .size(40.dp), // Adjust size as needed
                    tint = MaterialTheme.colorScheme.onPrimary // Icon color
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Add App Name
                Text(
                    text = "MyVisaApp",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}
