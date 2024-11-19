package com.uvg.uvgcare.firebase



import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uvg.uvgcare.firebase.LogIn.LoginDestination
import com.uvg.uvgcare.firebase.LogIn.loginScreen
import com.uvg.uvgcare.firebase.Navigation.MainScreen

@Composable
fun FirebaseApp(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = LoginDestination.ROUTE
    ) {
        loginScreen(
            onLoginSuccess = {
                navController.navigate("main") {
                    popUpTo(LoginDestination.ROUTE) { inclusive = true }
                }
            }
        )

        composable("main") {
            MainScreen()
        }
    }
}