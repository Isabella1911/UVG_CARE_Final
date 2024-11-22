package com.uvg.uvgcare

/*deje comentados algunos archivos que se repeteian ya que no queria borrarlos,
igual los subi asi al repo para que queden de referencia*/


import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.uvg.uvgcare.firebase.FavoritesList.FavoritesScreen
import com.uvg.uvgcare.firebase.LogIn.LoginDestination
import com.uvg.uvgcare.firebase.LogIn.loginScreen
import com.uvg.uvgcare.firebase.Navigation.MainBottomNavigation
import com.uvg.uvgcare.firebase.addThing.AddItemScreen
import com.uvg.uvgcare.firebase.home.NetflixStyleScreen


@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController()
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        bottomBar = {
            if (currentRoute != LoginDestination.ROUTE) {
                MainBottomNavigation(navController, currentRoute)
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = LoginDestination.ROUTE
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