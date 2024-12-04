package edu.ucne.fitgoal.presentation.calendario

sealed interface CalendarioEvent {
    data object GetCalendario: CalendarioEvent
}