package com.uvg.uvgcare.presentation.mainFlow

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
data object MainNavigationGraph {
    const val ROUTE = "main_navigation_graph"
}

fun NavController.navigateToMainGraph(navOptions: NavOptions? = null) {
    this.navigate(MainNavigationGraph.ROUTE, navOptions)
}

fun NavGraphBuilder.mainNavigationGraph(
    onLogOutClick: () -> Unit
) {
    composable(route = MainNavigationGraph.ROUTE) {
        val nestedNavController = rememberNavController()
        MainFlowScreen(
            navController = nestedNavController,
            onLogOutClick = onLogOutClick
        )
    }
}
