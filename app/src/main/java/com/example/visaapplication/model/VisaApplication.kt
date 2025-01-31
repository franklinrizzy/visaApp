//package com.example.visaapplication.model
//
//data class VisaApplication(
//    val id: String,
//    val country: String,
//    val passportNumber: String,
//    val address: String,
//    val additionalInfo: String,
//    val status: String
//)

package com.example.visaapplication.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class VisaApplication(
    val id: String = "",
    val country: String = "",
    val passportNumber: String = "",
    val address: String = "",
    val additionalInfo: String = "",
    val status: String = "Pending",
    val userId: String = "",
    @ServerTimestamp val timestamp: Timestamp = Timestamp.now()
)