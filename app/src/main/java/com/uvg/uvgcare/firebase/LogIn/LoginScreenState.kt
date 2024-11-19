package com.uvg.uvgcare.firebase.LogIn

data class LoginScreenState(
    val email: String = "",
    val password: String = "",
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val authStatus: Boolean = false,
    val isLoading: Boolean = false
)