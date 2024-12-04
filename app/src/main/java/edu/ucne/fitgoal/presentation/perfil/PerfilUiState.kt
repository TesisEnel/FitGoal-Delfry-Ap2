package edu.ucne.fitgoal.presentation.perfil

data class PerfilUiState(
    val nombre: String = "",
    val correo: String = "",
    val edad: Int = 0,
    val altura: Float = 0.0f,
    val pesoInicial: Float = 0.0f,
    val pesoActual: Float = 0.0f,
    val pesoIdeal: Float = 0.0f,
    val aguaDiaria: Float = 0.0f,
    val photoUrl: String? = null,
    val isLoading: Boolean = false,
    val error: String = "",
    val isModalErrorVisible: Boolean = false
)