package com.uvg.uvgcare.firebase.Repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.tasks.await

class FirebaseLoginRepository : LoginRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun login(email: String, password: String): Boolean {
        return try {
            Log.d("FirebaseAuth", "Intentando login con email: $email")
            auth.signInWithEmailAndPassword(email, password).await()
            Log.d("FirebaseAuth", "Login exitoso para: $email")
            true
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.e("FirebaseAuth", "Usuario no encontrado: $email")
            throw Exception("Usuario no encontrado")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.e("FirebaseAuth", "Credenciales inválidas para: $email")
            throw Exception("Contraseña incorrecta")
        } catch (e: Exception) {
            Log.e("FirebaseAuth", "Error en login: ${e.message}")
            throw Exception("Error al iniciar sesión: ${e.message}")
        }
    }

    override suspend fun createAccount(email: String, password: String): Boolean {
        return try {
            Log.d("FirebaseAuth", "Intentando crear cuenta con email: $email")
            auth.createUserWithEmailAndPassword(email, password).await()
            Log.d("FirebaseAuth", "Cuenta creada exitosamente para: $email")
            true
        } catch (e: FirebaseAuthWeakPasswordException) {
            Log.e("FirebaseAuth", "Contraseña débil")
            throw Exception("La contraseña debe tener al menos 6 caracteres")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.e("FirebaseAuth", "Email inválido: $email")
            throw Exception("El formato del correo es inválido")
        } catch (e: FirebaseAuthUserCollisionException) {
            Log.e("FirebaseAuth", "Email ya existe: $email")
            throw Exception("Este correo ya está registrado")
        } catch (e: Exception) {
            Log.e("FirebaseAuth", "Error al crear cuenta: ${e.message}")
            throw Exception("Error al crear la cuenta: ${e.message}")
        }
    }

    override suspend fun logout() {
        try {
            Log.d("FirebaseAuth", "Cerrando sesión del usuario: ${auth.currentUser?.email}")
            auth.signOut()
            Log.d("FirebaseAuth", "Sesión cerrada exitosamente")
        } catch (e: Exception) {
            Log.e("FirebaseAuth", "Error al cerrar sesión: ${e.message}")
            throw Exception("Error al cerrar sesión")
        }
    }

    override fun isUserLoggedIn(): Boolean {
        val isLoggedIn = auth.currentUser != null
        Log.d("FirebaseAuth", "Verificando estado de login: ${if (isLoggedIn) "Conectado" else "Desconectado"}")
        return isLoggedIn
    }

    override suspend fun sendPasswordResetEmail(email: String): Boolean {
        return try {
            Log.d("FirebaseAuth", "Enviando email de recuperación a: $email")
            auth.sendPasswordResetEmail(email).await()
            Log.d("FirebaseAuth", "Email de recuperación enviado exitosamente")
            true
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.e("FirebaseAuth", "Usuario no encontrado para recuperación: $email")
            throw Exception("No se encontró una cuenta con este correo")
        } catch (e: Exception) {
            Log.e("FirebaseAuth", "Error al enviar email de recuperación: ${e.message}")
            throw Exception("Error al enviar el correo de recuperación")
        }
    }

    companion object {
        private const val TAG = "FirebaseLoginRepository"
    }
}