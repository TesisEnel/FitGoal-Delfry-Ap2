package edu.ucne.fitgoal.presentation.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import edu.ucne.fitgoal.presentation.planificador.PlanificadorScreen
import edu.ucne.fitgoal.presentation.calculadora.CalculadoraScreen
import edu.ucne.fitgoal.presentation.perfil.PerfilScreen
import kotlinx.coroutines.launch
import androidx.navigation.compose.NavHost
import edu.ucne.fitgoal.presentation.reloj.RelojScreen

@Composable
fun FitGoalNavHost(
    navHostController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    Drawer(
        drawerState = drawerState,
        navHostController = navHostController
    ) {
        NavHost(
            navController = navHostController,
            startDestination = Screen.PlanificadorScreen
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
                        scope.launch {
                            drawerState.open()
                        }
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
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
            composable<Screen.RelojScreen> {
                RelojScreen(
                    goToPlanificador = {
                        navHostController.navigate(Screen.PlanificadorScreen)
                    },
                    goToPerfil = {
                        navHostController.navigate(Screen.PerfilScreen)
                    },
                    onDrawer = {
                        scope.launch {
                            drawerState.open()
                        }
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
                    onDrawer = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
        }
    }
}