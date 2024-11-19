package com.uvg.uvgcare.firebase.LogIn



import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object LoginDestination {
    const val ROUTE = "login_screen"
}

fun NavGraphBuilder.loginScreen(
    onLoginSuccess: () -> Unit
) {
    composable(route = LoginDestination.ROUTE) {
        LoginRoute(
            onLoginSuccess = onLoginSuccess
        )
    }
}