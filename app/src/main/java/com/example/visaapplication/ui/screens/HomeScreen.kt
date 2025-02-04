package com.example.visaapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    var searchQuery by remember { mutableStateOf("") }

    val visaDetails = listOf(
        Triple(R.drawable.bg_photoae, "United Arab Emirates", "07 Feb, 05:30 AM" to "₹6,500"),
        Triple(R.drawable.bg_photous, "United States", "15 Mar, 10:00 AM" to "₹22,000"),
        Triple(R.drawable.bg_photouk, "United Kingdom", "20 Apr, 08:45 AM" to "₹9,000"),
        Triple(R.drawable.bg_photoca, "Canada", "25 May, 02:15 PM" to "₹10,500"),
        Triple(R.drawable.bg_photosp, "Singapore", "20 May, 02:15 PM" to "₹1,900")
    )

    val filteredVisas = visaDetails.filter { it.second.contains(searchQuery, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopBar(searchQuery) { searchQuery = it }
        Spacer(modifier = Modifier.height(16.dp))

        filteredVisas.forEach { (imageRes, country, info) ->
            VisaCard(
                imageRes = imageRes,
                country = country,
                estimatedDate = info.first,
                price = info.second,
                navController = navController
            )
        }
    }
}

@Composable
fun TopBar(searchQuery: String, onSearchChange: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF6650a4)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Get Your Visas on Time",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                SearchBar(searchQuery, onSearchChange)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchQuery: String, onSearchChange: (String) -> Unit) {
    TextField(
        value = searchQuery,
        onValueChange = { onSearchChange(it) },
        placeholder = { Text("Enter destination") },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search Icon", tint = Color.Gray)
        },
        colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
    )
}

@Composable
fun VisaCard(imageRes: Int, country: String, estimatedDate: String, price: String, navController: NavController) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.clickable { navController.navigate("apply") }) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Visa Image",
                modifier = Modifier.fillMaxWidth().height(180.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = country, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "Get on $estimatedDate", fontSize = 14.sp, color = Color.Gray)
                Text(text = price, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { navController.navigate("apply/${country}") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Apply Now")
                }
            }
        }
    }
}
