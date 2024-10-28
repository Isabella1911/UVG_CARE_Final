package com.uvg.uvgcare.presentation.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object LoginDestination {
    const val ROUTE = "login_screen"
}

fun NavGraphBuilder.loginScreen(
    onLoginClick: () -> Unit
) {
    composable(route = LoginDestination.ROUTE) {
        LoginRoute(
            onLoginClick = onLoginClick
        )
    }
}


