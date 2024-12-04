package edu.ucne.fitgoal.presentation.Ejercicios

import edu.ucne.fitgoal.data.remote.dto.EjerciciosDto

data class EjerciciosUiState (
    val ejercicios: List<EjerciciosDto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)