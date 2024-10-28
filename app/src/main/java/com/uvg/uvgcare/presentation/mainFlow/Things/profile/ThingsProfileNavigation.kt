package com.uvg.uvgcare.presentation.mainFlow.Things.profile

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Serializable
data object CharacterProfileDestination {
    const val ROUTE = "character_profile/{id}"

    // Función para construir la ruta con el ID del personaje
    fun createRoute(id: Int): String {
        return "character_profile/$id"
    }
}

fun NavGraphBuilder.characterProfileScreen(
    onNavigateBack: () -> Unit
) {
    composable(
        route = CharacterProfileDestination.ROUTE,
        arguments = listOf(
            navArgument("id") { type = NavType.IntType } // Define el argumento de tipo Int
        )
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getInt("id") ?: 0 // Recupera el ID del personaje
        CharacterProfileRoute(
            id = id,
            onNavigateBack = onNavigateBack
        )
    }
}
