package com.example.visaapplication.model

data class VisaApplication(
    val id: String,
    val country: String,
    val passportNumber: String,
    val address: String,
    val additionalInfo: String,
    val status: String
)