package edu.ucne.fitgoal.presentation.ejercicio

import edu.ucne.fitgoal.data.local.entities.EjercicioEntity

data class UiState(
    val isLoading: Boolean = false,
    val error: String = "",
    val ejercicios: List<EjercicioEntity> = emptyList(),
    val isModalErrorVisible: Boolean = false
)
