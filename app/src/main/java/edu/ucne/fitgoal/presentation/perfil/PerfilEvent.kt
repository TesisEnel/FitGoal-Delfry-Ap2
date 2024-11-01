package edu.ucne.fitgoal.presentation.perfil

sealed interface PerfilEvent {
    data object GetPerfil : PerfilEvent
}