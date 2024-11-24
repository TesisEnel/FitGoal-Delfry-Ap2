package edu.ucne.fitgoal.presentation.ejercicio

interface EjercicioEvent {
    data object GetEjercicios: EjercicioEvent
    data object CloseErrorModal : EjercicioEvent
    data object CloseDetailModal : EjercicioEvent
    data class FilterEjercicios(val filtro: String): EjercicioEvent
    data class SelectEjercicio(val ejercicioId: Int): EjercicioEvent
}