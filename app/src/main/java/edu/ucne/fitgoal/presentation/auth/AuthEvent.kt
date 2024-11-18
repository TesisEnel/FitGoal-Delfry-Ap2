package edu.ucne.fitgoal.presentation.auth

import android.content.Context

sealed interface AuthEvent {
    data class NombreChanged(val nombre: String) : AuthEvent
    data class ApellidoChanged(val apellido: String) : AuthEvent
    data class EmailChanged(val email: String) : AuthEvent
    data class PasswordChanged(val password: String) : AuthEvent
    data class VerifyPasswordChanged(val verifyPassword: String) : AuthEvent
    data class ResetPassword(val email: String, val goLogin: () -> Unit) : AuthEvent
    data class SignInWithGoogle(val activityContext: Context, val goHome: () -> Unit) : AuthEvent
    data class SignUp(val goLogin: () -> Unit) : AuthEvent
    data class Logout(val context: Context, val goLogin: () -> Unit) : AuthEvent
    data object SignIn : AuthEvent
    data object StartDestination : AuthEvent
    data object SendEmailVerification : AuthEvent
    data object IsEmailVerified : AuthEvent
    data object StartEmailVerification : AuthEvent
    data object ChangePasswordVisibility : AuthEvent
}