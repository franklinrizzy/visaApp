package com.example.visaapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TrackScreen(navController: NavController) {
    val applications = remember { mutableStateListOf<VisaApplication>() }

    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        val result = db.collection("visaApplications").get().await()
        applications.clear()
        applications.addAll(result.toObjects(VisaApplication::class.java))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF6650A4)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Application Tracking",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(applications) { application ->
                ApplicationCard(application)
            }
        }
    }
}

@Composable
fun ApplicationCard(application: VisaApplication) {
    val appliedDateFormatted = application.appliedDate.ifEmpty {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Country: ${application.selectedCountry}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Applied Date: $appliedDateFormatted", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            StatusBar(application.status)
        }
    }
}

@Composable
fun StatusBar(status: String) {
    val statuses = listOf("Applied", "Under Review", "Rejected", "Accepted")
    val currentIndex = statuses.indexOf(status)

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        statuses.forEachIndexed { index, label ->
            val color = if (index <= currentIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    modifier = Modifier.size(12.dp),
                    shape = MaterialTheme.shapes.small,
                    color = color
                ) {}
                Text(text = label, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

data class VisaApplication(
    val selectedCountry: String = "",
    val appliedDate: String = "",
    val status: String = "Applied"
)
