package com.uvg.uvgcare.firebase.Navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.uvg.uvgcare.firebase.FavoritesList.FavoritesScreen
import com.uvg.uvgcare.firebase.LogIn.LoginDestination
import com.uvg.uvgcare.firebase.LogIn.loginScreen
import com.uvg.uvgcare.firebase.addThing.AddItemScreen
import com.uvg.uvgcare.firebase.home.NetflixStyleScreen

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController()
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != LoginDestination.ROUTE) {
                 MainBottomNavigation(navController, currentRoute)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = LoginDestination.ROUTE,
            modifier = Modifier.padding(paddingValues)
        ) {
            loginScreen(
                onLoginSuccess = {
                    navController.navigate(BottomNavItem.Home.route) {
                        popUpTo(LoginDestination.ROUTE) { inclusive = true }
                    }
                }
            )

            composable(BottomNavItem.Home.route) {
                NetflixStyleScreen()
            }

            composable(BottomNavItem.AddItem.route) {
                AddItemScreen(
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }

            composable(BottomNavItem.Favorites.route) {
                FavoritesScreen()
            }
        }
    }
}
