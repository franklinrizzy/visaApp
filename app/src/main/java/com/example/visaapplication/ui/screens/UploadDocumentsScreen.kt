package com.example.visaapplication.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.util.*

@Composable
fun UploadDocumentsScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val storageRef = FirebaseStorage.getInstance().reference
    val user = auth.currentUser

    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var uploadStatus by remember { mutableStateOf<String?>(null) }
    var uploadedFileName by remember { mutableStateOf<String?>(null) }

    val filePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedFileUri = uri
        uploadedFileName = uri?.lastPathSegment ?: "Unknown File"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Upload Documents",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))

        // File Selection Button
        Button(
            onClick = { filePickerLauncher.launch("application/pdf") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6650A4))
        ) {
            Icon(Icons.Default.FileUpload, contentDescription = "Upload Icon")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Select Passport File")
        }

        // Display Selected File
        selectedFileUri?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Selected file: $uploadedFileName",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Upload Button
            Button(
                onClick = {
                    user?.uid?.let { uid ->
                        val fileRef = storageRef.child("documents/$uid/${UUID.randomUUID()}.pdf")
                        selectedFileUri?.let { uri ->
                            fileRef.putFile(uri)
                                .addOnSuccessListener {
                                    fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                        uploadStatus = "Upload successful!"
                                        uploadedFileName = downloadUrl.toString()
                                    }
                                }
                                .addOnFailureListener {
                                    uploadStatus = "Upload failed: ${it.message}"
                                }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Green Button
            ) {
                Text("Upload Passport", color = Color.White)
            }
        }

        // Upload Status Message
        uploadStatus?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (it.contains("successful")) Color.Green else Color.Red
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Show Uploaded Document
        uploadedFileName?.let {
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F3F3))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Uploaded Document:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uploadedFileName ?: "No File",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Styled Cancel Button
        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF6650A4))
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cancel", fontSize = 16.sp)
        }
    }
}
