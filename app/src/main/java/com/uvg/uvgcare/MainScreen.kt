package com.uvg.uvgcare

/*deje comentados algunos archivos que se repeteian ya que no queria borrarlos,
igual los subi asi al repo para que queden de referencia*/

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
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
fun MainScreen() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != LoginDestination.ROUTE) {
                NavigationBar {
                    val items = listOf(
                        BottomNavItem.Home,
                        BottomNavItem.AddItem,
                        BottomNavItem.Favorites
                    )

                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentRoute == item.route,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
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

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : BottomNavItem(
        route = "home",
        icon = Icons.Default.Home,
        label = "Inicio"
    )

    object AddItem : BottomNavItem(
        route = "add_item",
        icon = Icons.Default.Add,
        label = "Publicar"
    )

    object Favorites : BottomNavItem(
        route = "favorites",
        icon = Icons.Default.Favorite,
        label = "Favoritos"
    )
}