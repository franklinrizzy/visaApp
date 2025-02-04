package com.example.visaapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ApplyScreen(navController: NavController, selectedCountry: String) {
    var applicantName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("+1") }
    var passportNumber by remember { mutableStateOf("") }
    var countryOfPassport by remember { mutableStateOf("United States") }
    var typeOfVisa by remember { mutableStateOf("") }
    var visaDuration by remember { mutableStateOf("") }
    var dateOfIssue by remember { mutableStateOf<Long?>(null) }
    var expiryDate by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf<Pair<Boolean, (Long?) -> Unit>?>(null) }

    val countries = listOf("Country of Residence", "United States", "Canada", "United Kingdom", "India", "Australia")
    val countryCodes = listOf("+1", "+91", "+44", "+61", "+1")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Visa Application", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6650A4))

        OutlinedTextField(
            value = applicantName,
            onValueChange = { applicantName = it },
            label = { Text("Applicant Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DropdownMenuBox(
                options = countryCodes,
                selectedOption = countryCode,
                onOptionSelected = { countryCode = it },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier.weight(2f)
            )
        }

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
            onOptionSelected = { countryOfPassport = it }
        )

        OutlinedTextField(
            value = typeOfVisa,
            onValueChange = { typeOfVisa = it },
            label = { Text("Type of Visa") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = visaDuration,
            onValueChange = { visaDuration = it.filter { char -> char.isDigit() } },
            label = { Text("Duration of Visa (in days)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val db = FirebaseFirestore.getInstance()
                val applicationData = hashMapOf(
                    "applicantName" to applicantName,
                    "phoneNumber" to "$countryCode$phoneNumber",
                    "passportNumber" to passportNumber,
                    "dateOfIssue" to dateOfIssue,
                    "expiryDate" to expiryDate,
                    "countryOfPassport" to countryOfPassport,
                    "typeOfVisa" to typeOfVisa,
                    "visaDuration" to visaDuration,
                    "selectedCountry" to selectedCountry
                )

                db.collection("visaApplications")
                    .add(applicationData)
                    .addOnSuccessListener { navController.navigate("home") }
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
