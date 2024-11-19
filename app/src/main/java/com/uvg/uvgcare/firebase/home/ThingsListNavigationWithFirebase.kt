package com.uvg.uvgcare.firebase.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object NetflixStyleDestination {
    const val ROUTE = "netflix_style_screen"
}

fun NavGraphBuilder.netflixStyleScreen() {
    composable(route = NetflixStyleDestination.ROUTE) {
        NetflixStyleScreen()  // Fixed function name
    }
}


