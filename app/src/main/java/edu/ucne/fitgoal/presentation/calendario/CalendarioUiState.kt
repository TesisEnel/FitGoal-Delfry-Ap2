package edu.ucne.fitgoal.presentation.calendario

import java.util.Calendar

data class CalendarioUiState(
    val selectedDay: Calendar? = null,
    val selectedDays: List<Calendar> = emptyList(),
    val toastMessage: String = "",
    val fecha: String = "",
    val hora: String = "",
    val estado: String = "Active",
    val nombreEvento: String = "Evento Desconocido",
    val id: Int = 0,
    val usuarioId: Int = 0
)