package com.uvg.uvgcare.presentation.mainFlow.list

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

@Serializable
data object NetflixStyleDestination {
    const val ROUTE = "netflix_style_screen"
}

fun NavGraphBuilder.netflixStyleScreen() {
    composable(route = NetflixStyleDestination.ROUTE) {
        NetflixStyleScreen()
    }
}
