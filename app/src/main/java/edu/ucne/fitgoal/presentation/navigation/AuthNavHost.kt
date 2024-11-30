package edu.ucne.fitgoal.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucne.fitgoal.presentation.auth.AuthViewModel
import edu.ucne.fitgoal.presentation.auth.LoginScreen
import edu.ucne.fitgoal.presentation.auth.ResetPassWordScreen
import edu.ucne.fitgoal.presentation.auth.RegistrarseScreen
import edu.ucne.fitgoal.presentation.auth.VerifyEmailScreen

@Composable
fun AuthNavHost(
    navHostController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    NavHost(
        navController = navHostController,
        startDestination = authViewModel.startDestination()
    ) {
        composable<Screen.VerifyScreen>{
            VerifyEmailScreen(
                goLogin = {
                    navHostController.navigate(Screen.LoginScreen) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                goHome = {
                    navHostController.navigate(Screen.MainNavHostScreen) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Screen.LoginScreen>{
            LoginScreen(
                goOlvidasteContrasena = {
                    navHostController.navigate(Screen.OlvidarContrasenaScreen)
                },
                goRegistro = {
                    navHostController.navigate(Screen.RegisterScreen)
                },
                goHome = {
                   navHostController.navigate(Screen.MainNavHostScreen) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                   }
                },
                goVerify = {
                    navHostController.navigate(Screen.VerifyScreen){
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Screen.RegisterScreen>{
            RegistrarseScreen(
                goLogin = {
                    navHostController.navigate(Screen.LoginScreen)
                }
            )
        }

        composable<Screen.OlvidarContrasenaScreen>{
            ResetPassWordScreen(
                goLogin = {
                    navHostController.navigate(Screen.LoginScreen)
                }
            )
        }

        composable<Screen.MainNavHostScreen>{
            MainNavHost()
        }
    }
}