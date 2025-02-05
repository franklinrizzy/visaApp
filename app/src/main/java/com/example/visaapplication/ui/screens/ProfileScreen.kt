package com.example.visaapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfileScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val user = auth.currentUser
    var userData by remember { mutableStateOf(UserData()) }

    LaunchedEffect(Unit) {
        user?.uid?.let { uid ->
            db.collection("users").document(uid).get().addOnSuccessListener { document ->
                document?.toObject(UserData::class.java)?.let {
                    userData = it
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(userData.profilePhotoUrl ?: "https://via.placeholder.com/150"),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable { /* Handle image selection */ },
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = userData.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(text = userData.email, fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileInfoItem(label = "Passport Number", value = userData.passportNumber)
                ProfileInfoItem(label = "Phone Number", value = userData.phoneNumber)
                ProfileInfoItem(label = "Date of Birth", value = userData.dateOfBirth)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { /* Handle edit details */ }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Details")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* Handle document upload */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Upload Documents")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                auth.signOut()
                navController.navigate("login")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Icon(Icons.Default.Logout, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sign Out")
        }
    }
}

@Composable
fun ProfileInfoItem(label: String, value: String?) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(text = value ?: "Not Available", fontSize = 16.sp, color = Color.Gray)
    }
}

data class UserData(
    val name: String = "",
    val email: String = "",
    val passportNumber: String = "",
    val phoneNumber: String = "",
    val dateOfBirth: String = "",
    val profilePhotoUrl: String? = null
)