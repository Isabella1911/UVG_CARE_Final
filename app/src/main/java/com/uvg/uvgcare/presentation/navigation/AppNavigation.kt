package com.uvg.uvgcare.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.uvg.uvgcare.presentation.login.LoginDestination
import com.uvg.uvgcare.presentation.login.loginScreen
import com.uvg.uvgcare.presentation.mainFlow.mainNavigationGraph
import com.uvg.uvgcare.presentation.mainFlow.navigateToMainGraph

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = LoginDestination.ROUTE,
        modifier = modifier
    ) {
        loginScreen(
            onLoginClick = {
                navController.navigateToMainGraph(
                    navOptions = NavOptions.Builder().setPopUpTo(LoginDestination.ROUTE, inclusive = true).build()
                )
            }
        )
        mainNavigationGraph(
            onLogOutClick = {
                navController.navigate(LoginDestination.ROUTE) {
                    popUpTo(0)
                }
            }
        )
    }
}
