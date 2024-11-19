package com.uvg.uvgcare.firebase.LogIn

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.uvg.uvgcare.firebase.LogIn.LoginScreenEvent.CreateAccountClicked
import com.uvg.uvgcare.firebase.LogIn.LoginScreenEvent.EmailChanged
import com.uvg.uvgcare.firebase.LogIn.LoginScreenEvent.ForgotPasswordClicked
import com.uvg.uvgcare.firebase.LogIn.LoginScreenEvent.LoginClicked
import com.uvg.uvgcare.firebase.LogIn.LoginScreenEvent.PasswordChanged
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _uiState = MutableStateFlow(LoginScreenState())
    val uiState: StateFlow<LoginScreenState> = _uiState.asStateFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        val currentUser = auth.currentUser
        Log.d("FirebaseAuth", "Checking auth status: ${currentUser?.email}")
        _uiState.update { it.copy(authStatus = currentUser != null) }
    }

    fun onEvent(event: LoginScreenEvent) {
        when (event) {
            is EmailChanged -> {
                _uiState.update {
                    it.copy(
                        email = event.email,
                        errorMessage = null
                    )
                }
            }
            is PasswordChanged -> {
                _uiState.update {
                    it.copy(
                        password = event.password,
                        errorMessage = null
                    )
                }
            }
            LoginClicked -> validateAndLogin()
            CreateAccountClicked -> validateAndCreateAccount()
            ForgotPasswordClicked -> handleForgotPassword()

        }
    }


    private fun validateAndCreateAccount() {
        val email = uiState.value.email
        val password = uiState.value.password

        when {
            email.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "Por favor ingrese su correo electrónico") }
                return
            }
            !isValidEmail(email) -> {
                _uiState.update { it.copy(errorMessage = "Por favor ingrese un correo válido") }
                return
            }
            password.isEmpty() -> {
                _uiState.update { it.copy(errorMessage = "Por favor ingrese su contraseña") }
                return
            }
            password.length < 6 -> {
                _uiState.update { it.copy(errorMessage = "La contraseña debe tener al menos 6 caracteres") }
                return
            }
        }

        createAccount()
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
        return email.matches(emailRegex)
    }

    private fun createAccount() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                Log.d("FirebaseAuth", "Intentando crear cuenta con email: ${uiState.value.email}")

                val result = auth.createUserWithEmailAndPassword(uiState.value.email, uiState.value.password).await()

                if (result.user != null) {
                    Log.d("FirebaseAuth", "Cuenta creada exitosamente con ID: ${result.user?.uid}")
                    _uiState.update {
                        it.copy(
                            successMessage = "Cuenta creada exitosamente",
                            isLoading = false,
                            authStatus = true
                        )
                    }
                } else {
                    throw Exception("Error al crear la cuenta: usuario nulo")
                }
            } catch (e: Exception) {
                Log.e("FirebaseAuth", "Error al crear cuenta: ${e.message}")
                handleAuthError(e)
            }
        }
    }

     fun validateAndLogin() {
        val email = uiState.value.email
        val password = uiState.value.password

        when {
            email.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "Por favor ingrese su correo electrónico") }
                return
            }
            !isValidEmail(email) -> {
                _uiState.update { it.copy(errorMessage = "Por favor ingrese un correo válido") }
                return
            }
            password.isEmpty() -> {
                _uiState.update { it.copy(errorMessage = "Por favor ingrese su contraseña") }
                return
            }
        }

        login()
    }

    private fun login() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                Log.d("FirebaseAuth", "Intentando login con email: ${uiState.value.email}")

                val result = auth.signInWithEmailAndPassword(uiState.value.email, uiState.value.password).await()

                if (result.user != null) {
                    Log.d("FirebaseAuth", "Login exitoso para usuario: ${result.user?.email}")
                    _uiState.update {
                        it.copy(
                            successMessage = "Inicio de sesión exitoso",
                            isLoading = false,
                            authStatus = true
                        )
                    }
                } else {
                    throw Exception("Error en login: usuario nulo")
                }
            } catch (e: Exception) {
                Log.e("FirebaseAuth", "Error en login: ${e.message}")
                handleAuthError(e)
            }
        }
    }
    private fun handleForgotPassword() {
        val email = uiState.value.email
        if (email.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Por favor ingrese su correo electrónico")
            }
            return
        }

        if (!isValidEmail(email)) {
            _uiState.update {
                it.copy(errorMessage = "Por favor ingrese un correo válido")
            }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                auth.sendPasswordResetEmail(email).await()
                _uiState.update {
                    it.copy(
                        successMessage = "Se ha enviado un correo para restablecer su contraseña",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                handleAuthError(e)
            }
        }
    }
    private fun handleAuthError(e: Exception) {
        val errorMessage = when {
            e.message?.contains("badly formatted") == true -> "Formato de correo inválido"
            e.message?.contains("password is invalid") == true -> "Contraseña incorrecta"
            e.message?.contains("no user record") == true -> "Usuario no encontrado"
            e.message?.contains("network error") == true -> "Error de conexión"
            e.message?.contains("email already in use") == true -> "El correo ya está registrado"
            e.message?.contains("weak password") == true -> "La contraseña es muy débil"
            else -> "Error de autenticación: ${e.message}"
        }

        Log.e("FirebaseAuth", "Error manejado: $errorMessage")

        _uiState.update {
            it.copy(
                errorMessage = errorMessage,
                isLoading = false,
                authStatus = false
            )
        }
    }

    // ... resto del código igual ...

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel() as T
            }
        }
    }
}

@Composable
fun LoginRoute(
    viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory),
    onLoginSuccess: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Manejar mensajes de error
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    // Manejar mensajes de éxito
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    // Manejar navegación solo cuando authStatus es true
    LaunchedEffect(uiState.authStatus) {
        if (uiState.authStatus) {
            onLoginSuccess()
        }
    }

    LoginScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = Modifier.fillMaxSize()
    )
}