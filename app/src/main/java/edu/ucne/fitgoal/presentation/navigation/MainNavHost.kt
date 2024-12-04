package edu.ucne.fitgoal.presentation.navigation

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucne.fitgoal.presentation.calendario.CalendarioScreen
import edu.ucne.fitgoal.presentation.home.HomeScreen
import edu.ucne.fitgoal.presentation.plantilla.CrearRutinaScreen
import edu.ucne.fitgoal.presentation.plantilla.PlantillaScreen
import edu.ucne.fitgoal.MainActivity
import edu.ucne.fitgoal.presentation.aguadiaria.HorarioScreen
import edu.ucne.fitgoal.presentation.calculadora.CalculadoraScreen
import edu.ucne.fitgoal.presentation.ejercicio.EjercicioScreen
import edu.ucne.fitgoal.presentation.home.HomeScreen
import edu.ucne.fitgoal.presentation.perfil.PerfilScreen
import edu.ucne.fitgoal.presentation.planificador.PlanificadorScreen


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
            composable<Screen.PerfilScreen> {
                PerfilScreen(
                    navController = navHostController
                )
            }

            composable<Screen.AguaDiariaScreen> {
                HorarioScreen()
            }

            composable<Screen.EjerciciosScreen> {
                EjercicioScreen()
            }

            composable<Screen.CalculadoraScreen> {
                CalculadoraScreen(

                    goToPlanificador = {
                        navHostController.navigate(Screen.PlanificadorScreen)
                    }
                )
            }
            composable<Screen.HomeScreen> {
                HomeScreen(
                    goPerfil = {
                        navHostController.navigate(Screen.PerfilScreen)
                    }
                )
            }

            composable<Screen.AuthNavHostScreen> {
                val context = LocalContext.current
                LaunchedEffect(Unit) {
                    (context as? Activity)?.finish()
                    context.startActivity(
                        Intent(context, MainActivity::class.java)
                    )
                }
            }
        }
    }
}