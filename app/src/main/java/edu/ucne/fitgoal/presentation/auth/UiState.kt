package edu.ucne.fitgoal.presentation.auth

import androidx.compose.ui.text.input.KeyboardType
import edu.ucne.fitgoal.data.remote.dto.UsuarioDto

data class UiState(
    val nombre: String = "",
    val nombreError: String = "",
    val apellido: String = "",
    val apellidoError: String = "",
    val email: String = "",
    val emailError: String = "",
    val edad: Int = 0,
    val edadError: String = "",
    val altura: Float = 0f,
    val alturaError: String = "",
    val pesoInicial: Float = 0f,
    val pesoInicialError: String = "",
    val pesoActual: Float = 0f,
    val pesoIdeal: Float = 0f,
    val pesoIdealError: String = "",
    val pesoActualError: String = "",
    val password: String = "",
    val verifyPassword: String = "",
    val passwordError: String = "",
    val verifyPasswordError: String = "",
    val isLoading: Boolean = false,
    val error: String = "",
    val canSeePassword: Boolean = false,
    val keyboardType: KeyboardType = KeyboardType.Password,
    val isAuthorized: Boolean = false,
    val isDatosLLenos: Boolean = false,
    var isEmailVerified: Boolean = false,
    val isButtonEnabled: Boolean = true,
    val tiempo: Int = 0,
    val update: Boolean = false,
    val esNuevo: Boolean = false,
    val isModalErrorVisible: Boolean = false,
)

fun UiState.toDto() = UsuarioDto(
    usuarioId = "",
    nombre = nombre,
    apellido = apellido,
    correo = email
)

fun UiState.toUsuarioDto() = UsuarioDto(
    usuarioId = "",
    nombre = nombre,
    apellido = apellido,
    correo = email,
    edad = edad,
    altura = altura,
    pesoInicial = pesoInicial,
    pesoActual = pesoActual,
    pesoIdeal = pesoIdeal
)