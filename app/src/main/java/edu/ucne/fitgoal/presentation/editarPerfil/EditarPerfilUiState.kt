package edu.ucne.fitgoal.presentation.editarPerfil

data class EditarPerfilUiState(
    val usuarioId: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val edad: Int = 0,
    val altura: Float = 0.0f,
    val pesoInicial: Float = 0.0f,
    val pesoActual: Float = 0.0f,
    val pesoIdeal: Float = 0.0f,
    val aguaDiaria: Float = 0.0f,
    val isLoading: Boolean = false,
    val error: String = ""
)
