package edu.ucne.fitgoal.presentation.home

sealed class HomeEvent {
    data object FetchTips : HomeEvent()
    data object CloseErrorModal : HomeEvent()
}