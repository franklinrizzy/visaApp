package com.example.visaapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ApplyScreen(navController: NavController, selectedCountry: String) {
    var applicantName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var passportNumber by remember { mutableStateOf("") }
    var countryOfPassport by remember { mutableStateOf("Select Country") }
    var visaType by remember { mutableStateOf("Select Visa Type") }
    var visaDuration by remember { mutableStateOf("") }
    var dateOfIssue by remember { mutableStateOf<Long?>(null) }
    var expiryDate by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf<Pair<Boolean, (Long?) -> Unit>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val countries = listOf("Select Country", "United States", "Canada", "United Kingdom", "India", "Australia")
    val visaTypes = listOf("Tourist", "Business", "Employment", "Student", "Transit", "Medical", "Conference", "Journalist")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Enable scrolling
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Visa Application",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6650A4)
        )

        OutlinedTextField(
            value = applicantName,
            onValueChange = { applicantName = it },
            label = { Text("Applicant Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it.filter { char -> char.isDigit() } },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = passportNumber,
            onValueChange = { passportNumber = it },
            label = { Text("Passport Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { showDatePicker = true to { dateOfIssue = it } },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = dateOfIssue?.let { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(it)) }
                        ?: "Date of Issue"
                )
            }

            Button(
                onClick = { showDatePicker = true to { expiryDate = it } },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = expiryDate?.let { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(it)) }
                        ?: "Date of Expiry"
                )
            }
        }

        DropdownMenuBox(
            options = countries,
            selectedOption = countryOfPassport,
            onOptionSelected = { countryOfPassport = it },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenuBox(
            options = visaTypes,
            selectedOption = visaType,
            onOptionSelected = { visaType = it },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = visaDuration,
            onValueChange = { visaDuration = it.filter { char -> char.isDigit() } },
            label = { Text("Duration of Visa (in days)") },
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage != null) {
            Text(text = errorMessage!!, color = Color.Red, fontSize = 14.sp)
        }

        var showSnackbar by remember { mutableStateOf(false) }
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        Button(
            onClick = {
                if (applicantName.isEmpty() || phoneNumber.isEmpty() || passportNumber.isEmpty() ||
                    countryOfPassport == "Select Country" || visaType == "Select Visa Type" || visaDuration.isEmpty()
                ) {
                    errorMessage = "Please fill all required fields!"
                    return@Button
                }

                val userId = currentUser?.uid ?: return@Button // Ensure user is logged in

                val db = FirebaseFirestore.getInstance()
                val applicationData = hashMapOf(
                    "userId" to userId,
                    "applicantName" to applicantName,
                    "phoneNumber" to phoneNumber,
                    "passportNumber" to passportNumber,
                    "dateOfIssue" to dateOfIssue,
                    "expiryDate" to expiryDate,
                    "countryOfPassport" to countryOfPassport,
                    "visaType" to visaType,
                    "visaDuration" to visaDuration,
                    "selectedCountry" to selectedCountry,
                    "status" to "Applied"
                )

                db.collection("visaApplications")
                    .add(applicationData)
                    .addOnSuccessListener {
                        showSnackbar = true
                        navController.navigate("dummy_payment")
                    }
                    .addOnFailureListener { e -> println("Error: ${e.message}") }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6650A4))
        ) {
            Text("Submit Application", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        if (showSnackbar) {
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(onClick = { showSnackbar = false }) {
                        Text("OK", color = Color.White)
                    }
                }
            ) {
                Text("Application submitted successfully!")
            }
        }
    }

    showDatePicker?.let { (show, onDateSelected) ->
        if (show) {
            DatePickerModal(onDateSelected = onDateSelected, onDismiss = { showDatePicker = null })
        }
    }
}

@Composable
fun DropdownMenuBox(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6650A4))
        ) {
            Text(selectedOption, color = Color.White)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
