package edu.ucne.fitgoal.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucne.fitgoal.presentation.calculadora.CalculadoraScreen
import edu.ucne.fitgoal.presentation.calendario.CalendarioScreen
import edu.ucne.fitgoal.presentation.home.HomeScreen
import edu.ucne.fitgoal.presentation.plantilla.CrearRutinaScreen
import edu.ucne.fitgoal.presentation.plantilla.PlantillaScreen


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
                PlantillaScreen(
                    goToPerfil = {
                        navHostController.navigate(Screen.PerfilScreen)
                    },
                    goToReloj = {
                        navHostController.navigate(Screen.RelojScreen)
                    },
                    goToCrearRutina = {
                        navHostController.navigate(Screen.CrearRutinaScreen)
                    },
                    goToCalculadora = {
                        navHostController.navigate(Screen.CalculadoraScreen)
                    }


                )
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
                    goToCalendario = {
                        navHostController.navigate(Screen.CalendarioScreen)
                    },
                    onDrawer = {
                        navHostController.navigate(Screen.HomeScreen)
                    }
                )
            }
            composable<Screen.CrearRutinaScreen> {
                CrearRutinaScreen(
                    goToPlanificador = {
                        navHostController.navigate(Screen.PlanificadorScreen)
                    },
                    goToPerfil = {
                        navHostController.navigate(Screen.PerfilScreen)
                    },
                    goToReloj = {
                        navHostController.navigate(Screen.RelojScreen)
                    },
                    goToCalculadora = {
                        navHostController.navigate(Screen.CalculadoraScreen)
                    }
                )
            }
            composable<Screen.CalendarioScreen> {
                CalendarioScreen(
                    goToPlanificador = {
                        navHostController.navigate(Screen.PlanificadorScreen)
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