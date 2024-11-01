package edu.ucne.fitgoal.presentation.calculadora

sealed interface CalculadoraEvent {
    data object GetCalculadora: CalculadoraEvent
}