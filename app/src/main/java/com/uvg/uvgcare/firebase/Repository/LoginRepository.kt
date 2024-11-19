package com.uvg.uvgcare.firebase.Repository

// LoginRepository.kt

interface LoginRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun createAccount(email: String, password: String): Boolean  // Cambiado de createUser a createAccount
    suspend fun logout()
    fun isUserLoggedIn(): Boolean
    suspend fun sendPasswordResetEmail(email: String): Boolean
}