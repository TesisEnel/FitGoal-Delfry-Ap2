package edu.ucne.fitgoal.presentation.perfil

sealed interface PerfilEvent {
    data object GetPerfil : PerfilEvent
    data object NavigateToEditarPerfil : PerfilEvent
    data object NavigateToProgresoSemanal : PerfilEvent
    data object Logout : PerfilEvent
    data object CloseErrorModal : PerfilEvent
}