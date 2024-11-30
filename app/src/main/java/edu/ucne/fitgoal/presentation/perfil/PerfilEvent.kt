package edu.ucne.fitgoal.presentation.perfil

sealed interface PerfilEvent {
    data object GetPerfil : PerfilEvent
    data object NavigateToEditarPerfil : PerfilEvent
    data object NavigateToCalculadora : PerfilEvent
    data object NavigateToTips : PerfilEvent
    data object NavigateToAyuda : PerfilEvent
    data object Logout : PerfilEvent
    data object CloseErrorModal : PerfilEvent
}