package com.example.visaapplication.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

data class UserData(
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val dateOfBirth: String = "",
    val profilePhotoUrl: String? = null
)

@Composable
fun ProfileScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val storageRef = FirebaseStorage.getInstance().reference
    val user = auth.currentUser

    var userData by remember { mutableStateOf(UserData()) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var uploadStatus by remember { mutableStateOf<String?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedImageUri = uri
        uri?.let { uploadProfilePicture(it, user?.uid, storageRef) }
    }

    // Load user data from Firestore
    LaunchedEffect(Unit) {
        user?.uid?.let { uid ->
            try {
                val document = db.collection("users").document(uid).get().await()
                val data = document.toObject(UserData::class.java)
                if (data != null) {
                    userData = data
                }
            } catch (e: Exception) {
                uploadStatus = "Failed to load profile: ${e.message}"
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Profile Image Section
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable { imagePickerLauncher.launch("image/*") }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        selectedImageUri ?: userData.profilePhotoUrl
                        ?: "https://cdn-icons-png.flaticon.com/512/149/149071.png"
                    ),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Tap to change profile picture",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = userData.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = userData.email, fontSize = 16.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Info Card
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    ProfileInfoItem(label = "Phone Number", value = userData.phoneNumber)
                    ProfileInfoItem(label = "Date of Birth", value = userData.dateOfBirth)

                    Spacer(modifier = Modifier.height(8.dp))

                    // Edit Profile Button
                    Button(
                        onClick = { navController.navigate("edit_profile") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6650A4))
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Edit Details")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Upload Documents Button
            Button(
                onClick = { navController.navigate("upload_documents") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6650A4))
            ) {
                Text("Upload Documents")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Logout Button
            Button(
                onClick = {
                    auth.signOut()
                    navController.navigate("login")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign Out")
            }
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

// Function to Upload Profile Picture to Firebase Storage
fun uploadProfilePicture(imageUri: Uri, userId: String?, storageRef: com.google.firebase.storage.StorageReference) {
    if (userId == null) return

    val profilePicRef = storageRef.child("profile_pictures/$userId.jpg")
    profilePicRef.putFile(imageUri)
        .addOnSuccessListener {
            profilePicRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                // Update Firestore with the new profile picture URL
                FirebaseFirestore.getInstance().collection("users").document(userId)
                    .update("profilePhotoUrl", downloadUrl.toString())
            }
        }
}
