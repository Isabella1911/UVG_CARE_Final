package com.uvg.uvgcare.presentation.mainFlow.Things.favlist

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

@Serializable
data object FavoritesDestination {
    const val ROUTE = "favorites_screen"
}

fun NavGraphBuilder.favoritesScreen() {
    composable(route = FavoritesDestination.ROUTE) {
        FavoritesScreen() // Llama a la función actual de la pantalla de favoritos
    }
}
