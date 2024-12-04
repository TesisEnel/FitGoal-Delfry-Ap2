package edu.ucne.fitgoal.presentation.ejercicio

import edu.ucne.fitgoal.data.local.entities.EjercicioEntity
import edu.ucne.fitgoal.data.remote.dto.EjerciciosDto

data class UiState(
    val isLoading: Boolean = false,
    val error: String = "",
    val ejercicios: List<EjercicioEntity> = emptyList(),
    val selectedEjercicio: EjercicioEntity? = null,
    val isModalErrorVisible: Boolean = false,
    val isModalDetailVisible: Boolean = false,
    val filtro: String = "",
    val errorMessage: String? = null
)


