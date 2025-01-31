//package com.example.visaapplication.ui.screens
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import androidx.compose.ui.Alignment
//
//@Composable
//fun SignupScreen(navController: NavController) {
//    var name by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var passportNumber by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
//        Spacer(modifier = Modifier.height(8.dp))
//        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
//        Spacer(modifier = Modifier.height(8.dp))
//        TextField(value = passportNumber, onValueChange = { passportNumber = it }, label = { Text("Passport Number") })
//        Spacer(modifier = Modifier.height(8.dp))
//        TextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            visualTransformation = PasswordVisualTransformation()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = {
//            // Sign Up Logic Here
//            navController.navigate("login")
//        }) {
//            Text("Sign Up")
//        }
//    }
//}

package com.example.visaapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.visaapplication.model.User

@Composable
fun SignupScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passportNumber by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val auth = Firebase.auth
    val db = Firebase.firestore

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = passportNumber, onValueChange = { passportNumber = it }, label = { Text("Passport Number") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Display error message if signup fails
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(onClick = {
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && passportNumber.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Save additional user data to Firestore
                            val user = User(name, email, password, passportNumber)
                            val userId = auth.currentUser?.uid
                            if (userId != null) {
                                db.collection("users")
                                    .document(userId)
                                    .set(user)
                                    .addOnSuccessListener {
                                        // Navigate to LoginScreen after successful signup
                                        navController.navigate("login") {
                                            popUpTo("signup") { inclusive = true }
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        errorMessage = "Failed to save user data: ${e.message}"
                                    }
                            }
                        } else {
                            // Show error message
                            errorMessage = task.exception?.message ?: "Signup failed"
                        }
                    }
            } else {
                errorMessage = "Please fill in all fields"
            }
        }) {
            Text("Sign Up")
        }
    }
}