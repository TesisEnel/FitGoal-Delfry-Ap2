package edu.ucne.fitgoal.presentation.ejercicio

interface EjercicioEvent {
    data object GetEjercicios: EjercicioEvent
    data object CloseErrorModal : EjercicioEvent
}