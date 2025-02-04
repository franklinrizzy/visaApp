//package com.example.visaapplication.ui.screens
//
//import android.widget.Toast
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import com.stripe.android.PaymentConfiguration
//import com.stripe.android.paymentsheet.PaymentSheet
//import com.stripe.android.paymentsheet.PaymentSheetResult
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import org.json.JSONObject
//import java.net.HttpURLConnection
//import java.net.URL
//
//@Composable
//fun PaymentScreen() {
//    val context = LocalContext.current
//    val coroutineScope = rememberCoroutineScope()
//
//    // Initialize Stripe Payment Sheet
//    val paymentSheet = remember { PaymentSheet(context as androidx.activity.ComponentActivity) { result ->
//        handlePaymentResult(result, context)
//    } }
//
//    // Fetch Payment Intent Client Secret
//    var clientSecret by remember { mutableStateOf<String?>(null) }
//
//    LaunchedEffect(Unit) {
//        coroutineScope.launch {
//            clientSecret = fetchPaymentIntent()
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        if (clientSecret != null) {
//            Button(onClick = {
//                paymentSheet.presentWithPaymentIntent(
//                    clientSecret!!,
//                    PaymentSheet.Configuration(
//                        merchantDisplayName = "Visa Application"
//                    )
//                )
//            }) {
//                Text("Pay Now")
//            }
//        } else {
//            CircularProgressIndicator()
//        }
//    }
//}
//
//private suspend fun fetchPaymentIntent(): String? {
//    return withContext(Dispatchers.IO) {
//        try {
//            val url = URL("https://your-backend-url/create-payment-intent")
//            val connection = url.openConnection() as HttpURLConnection
//            connection.requestMethod = "POST"
//            connection.doOutput = true
//            connection.setRequestProperty("Content-Type", "application/json")
//
//            val json = JSONObject().apply {
//                put("amount", 5000) // Amount in cents ($50)
//                put("currency", "usd")
//            }
//
//            connection.outputStream.use { it.write(json.toString().toByteArray()) }
//
//            val response = connection.inputStream.bufferedReader().readText()
//            val responseJson = JSONObject(response)
//
//            responseJson.getString("clientSecret")
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
//}
//
//private fun handlePaymentResult(result: PaymentSheetResult, context: android.content.Context) {
//    when (result) {
//        is PaymentSheetResult.Completed -> {
//            Toast.makeText(context, "Payment Successful", Toast.LENGTH_SHORT).show()
//        }
//        is PaymentSheetResult.Canceled -> {
//            Toast.makeText(context, "Payment Canceled", Toast.LENGTH_SHORT).show()
//        }
//        is PaymentSheetResult.Failed -> {
//            Toast.makeText(context, "Payment Failed: ${result.error.message}", Toast.LENGTH_SHORT).show()
//        }
//    }
//}