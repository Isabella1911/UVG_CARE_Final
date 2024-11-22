package com.uvg.uvgcare.firebase.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object AuthUtils {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }

    // Función de utilidad para verificar autenticación y lanzar excepción si no hay usuario
    fun requireCurrentUserId(): String {
        return getCurrentUserId() ?: throw Exception("Usuario no autenticado")
    }
}