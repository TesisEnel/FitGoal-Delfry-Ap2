package edu.ucne.fitgoal.presentation.reloj

sealed interface RelojEvent {
    data object GetReloj : RelojEvent
}