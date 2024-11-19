package com.uvg.uvgcare.firebase.LogIn

sealed class LoginScreenEvent {
    data class EmailChanged(val email: String) : LoginScreenEvent()
    data class PasswordChanged(val password: String) : LoginScreenEvent()
    object LoginClicked : LoginScreenEvent()
    object CreateAccountClicked : LoginScreenEvent()
    object ForgotPasswordClicked : LoginScreenEvent()
}
