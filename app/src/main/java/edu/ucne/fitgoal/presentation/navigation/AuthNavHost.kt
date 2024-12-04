package edu.ucne.fitgoal.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucne.fitgoal.presentation.auth.AuthEvent
import edu.ucne.fitgoal.presentation.auth.AuthViewModel
import edu.ucne.fitgoal.presentation.auth.DatosUsuarioScreen
import edu.ucne.fitgoal.presentation.auth.LoginScreen
import edu.ucne.fitgoal.presentation.auth.ResetPassWordScreen
import edu.ucne.fitgoal.presentation.auth.RegistrarseScreen
import edu.ucne.fitgoal.presentation.auth.VerifyEmailScreen
import edu.ucne.fitgoal.presentation.components.ShowComponent

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AuthNavHost(
    navHostController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

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
            ShowComponent(
                value = uiState.isDatosLLenos,
                whenContentIsTrue = {
                    MainNavHost()
                }
            )
            ShowComponent(
                value = uiState.edad <= 0,
                whenContentIsTrue = {
                    DatosUsuarioScreen(
                        onCerrarSesion = {
                            navHostController.navigate(Screen.LoginScreen){
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onGoHome = {
                            navHostController.navigate(Screen.Home){
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            )
        }

        composable<Screen.Home> {
            MainNavHost()
        }
    }
}