package com.example.visaapplication.ui.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun DummyPaymentScreen(navController: NavController) {
    var selectedPaymentMethod by remember { mutableStateOf("Card") }
    var upiId by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6650A4))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Payment", fontSize = 24.sp, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Select Payment Method", fontSize = 18.sp, color = Color.Black)

                Spacer(modifier = Modifier.height(16.dp))

                // Payment Method Selection
                PaymentOption("Card", selectedPaymentMethod) { selectedPaymentMethod = it }
                Spacer(modifier = Modifier.height(8.dp))
                PaymentOption("UPI", selectedPaymentMethod) { selectedPaymentMethod = it }

                Spacer(modifier = Modifier.height(16.dp))

                // Show UPI or Card fields based on selection
                if (selectedPaymentMethod == "UPI") {
                    OutlinedTextField(
                        value = upiId,
                        onValueChange = { upiId = it },
                        label = { Text("Enter UPI ID") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                } else {
                    OutlinedTextField(
                        value = cardNumber,
                        onValueChange = { cardNumber = it.filter { char -> char.isDigit() }.take(16) }, // Allow only digits, max 16
                        label = { Text("Card Number") },
                        leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = expiryDate,
                            onValueChange = { expiryDate = it.take(5) }, // Max 5 chars (MM/YY)
                            label = { Text("Expiry Date (MM/YY)") },
                            modifier = Modifier.weight(1f),
                        )

                        OutlinedTextField(
                            value = cvv,
                            onValueChange = { cvv = it.filter { char -> char.isDigit() }.take(3) }, // Allow only digits, max 3
                            label = { Text("CVV") },
                            modifier = Modifier.weight(1f),
                            visualTransformation = VisualTransformation.None
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Proceed Button
                Button(
                    onClick = {
                        navController.navigate("track") // Simulating Payment Success
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6650A4))
                ) {
                    Text("Proceed to Payment", color = Color.White)
                }
            }
        }
    }
}

// Payment Selection Option
@Composable
fun PaymentOption(title: String, selected: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = title == selected,
            onClick = { onSelect(title) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, fontSize = 16.sp, color = Color.Black)
    }
}
