package com.example.visaapplication.repository

import com.example.visaapplication.model.User

class AuthRepository {
    fun login(email: String, password: String): Boolean {
        // Logic for user login
        return true // Replace with actual authentication logic
    }

    fun signup(user: User): Boolean {
        // Logic for user signup
        return true // Replace with actual signup logic
    }
}
