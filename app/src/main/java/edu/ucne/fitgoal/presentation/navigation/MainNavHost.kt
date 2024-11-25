package edu.ucne.fitgoal.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucne.fitgoal.presentation.calculadora.CalculadoraScreen
import edu.ucne.fitgoal.presentation.ejercicio.EjercicioScreen
import edu.ucne.fitgoal.presentation.home.HomeScreen
import edu.ucne.fitgoal.presentation.perfil.PerfilScreen
import edu.ucne.fitgoal.presentation.planificador.PlanificadorScreen
import edu.ucne.fitgoal.presentation.reloj.RelojScreen

@Composable
fun MainNavHost(
    navHostController: NavHostController = rememberNavController()
) {
    val selectedItem = remember { mutableStateOf("Home") }

    BoxBottomNav(
        navHostController = navHostController,
        selectedItem = selectedItem
    ) {
        NavHost(
            navController = navHostController,
            startDestination = Screen.HomeScreen,
        ) {
            composable<Screen.PlanificadorScreen> {
                PlanificadorScreen(
                    goToPerfil = {
                        navHostController.navigate(Screen.PerfilScreen)
                    },
                    goToReloj = {
                        navHostController.navigate(Screen.RelojScreen)
                    },
                    onDrawer = {

                    }
                )
            }

            composable<Screen.PerfilScreen> {
                PerfilScreen(
                    goToPlanificador = {
                        navHostController.navigate(Screen.PlanificadorScreen)
                    },
                    goToReloj = {
                        navHostController.navigate(Screen.RelojScreen)
                    },
                    onDrawer = {

                    }
                )
            }
            composable<Screen.RelojScreen> {
                EjercicioScreen()
            }
            composable<Screen.CalculadoraScreen> {

                CalculadoraScreen(
                    goToPlanificador = {
                        navHostController.navigate(Screen.PlanificadorScreen)
                    },
                    goToPerfil = {
                        navHostController.navigate(Screen.PerfilScreen)
                    },
                    goToReloj = {
                        navHostController.navigate(Screen.RelojScreen)
                    },
                    onDrawer = {

                    }
                )
            }
            composable<Screen.HomeScreen> {
                HomeScreen()
            }

            composable<Screen.AuthNavHostScreen> {
                AuthNavHost()
            }
        }
    }
}