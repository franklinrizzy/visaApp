package com.example.visaapplication.repository

import com.example.visaapplication.model.VisaApplication

class VisaRepository {
    fun submitApplication(application: VisaApplication): Boolean {
        // Logic to submit visa application
        return true // Replace with actual backend call
    }

    fun trackApplication(passportNumber: String): String {
        // Logic to track visa application
        return "Under Review" // Replace with actual backend call
    }
}
