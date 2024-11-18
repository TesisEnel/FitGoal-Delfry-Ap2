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
    val password: String = "",
    val verifyPassword: String = "",
    val passwordError: String = "",
    val verifyPasswordError: String = "",
    val isLoading: Boolean = false,
    val error: String = "",
    val canSeePassword: Boolean = false,
    val keyboardType: KeyboardType = KeyboardType.Password,
    val isAuthorized: Boolean = false,
    var isEmailVerified: Boolean = false,
    val isButtonEnabled: Boolean = true,
    val tiempo: Int = 0,
    val isModalErrorVisible: Boolean = false,
)

fun UiState.toDto() = UsuarioDto(
    usuarioId = "",
    nombre = nombre,
    apellido = apellido,
    correo = email
)