package edu.ucne.fitgoal.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object PlanificadorScreen : Screen()

    @Serializable
    data object PerfilScreen : Screen()

    @Serializable
    data object RelojScreen : Screen()

    @Serializable
    data object CalculadoraScreen : Screen()
}