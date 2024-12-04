package edu.ucne.fitgoal.presentation.editarPerfil

sealed interface EditarPerfilEvent {
    data class UpdateEdad(val edad: Int) : EditarPerfilEvent
    data class UpdateAltura(val altura: Float) : EditarPerfilEvent
    data class UpdatePesoInicial(val pesoInicial: Float) : EditarPerfilEvent
    data class UpdatePesoActual(val pesoActual: Float) : EditarPerfilEvent
    data class UpdatePesoIdeal(val pesoIdeal: Float) : EditarPerfilEvent
    data class UpdateAguaDiaria(val aguaDiaria: Float) : EditarPerfilEvent
    data object GuardarCambios : EditarPerfilEvent
}
