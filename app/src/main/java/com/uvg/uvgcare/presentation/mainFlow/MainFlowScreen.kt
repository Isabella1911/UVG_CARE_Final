package com.uvg.uvgcare.presentation.mainFlow

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.uvg.uvgcare.presentation.mainFlow.Things.favlist.FavoritesDestination
import com.uvg.uvgcare.presentation.mainFlow.Things.favlist.favoritesScreen
import com.uvg.uvgcare.presentation.mainFlow.addThing.characterProfileScreen
import com.uvg.uvgcare.presentation.mainFlow.list.netflixStyleScreen
import com.uvg.uvgcare.presentation.navigation.BottomNavBar
import com.uvg.uvgcare.presentation.navigation.topLevelDestinations

@Composable
fun MainFlowScreen(
    onLogOutClick: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    var bottomBarVisible by rememberSaveable { mutableStateOf(false) }
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    bottomBarVisible = if (currentDestination != null) {
        topLevelDestinations.any { destination ->
            currentDestination.route == destination
        }
    } else {
        false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
            ) {
                BottomNavBar(
                    checkItemSelected = { destination ->
                        currentDestination?.hierarchy?.any { it.route == destination } ?: false
                    },
                    onNavItemClick = { destination ->
                        navController.navigate(destination) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = FavoritesDestination.ROUTE,
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            netflixStyleScreen()
            favoritesScreen()
            characterProfileScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}


