package edu.ucne.fitgoal.presentation.home

sealed class HomeEvent {
    data object UpdateProgreso : HomeEvent()
    data object CloseErrorModal : HomeEvent()
    data object CloseModal : HomeEvent()
    data object OpenRegistrarProgreso : HomeEvent()
    data object OpenNuevaMeta : HomeEvent()
    data object SaveProgreso : HomeEvent()
    data class PesoChanged(val peso: String) : HomeEvent()
    data class PesoIdealChanged(val pesoIdeal: String) : HomeEvent()
    data class PesoInicialChanged(val pesoInicial: String) : HomeEvent()
}