package edu.ucne.fitgoal.presentation.planificador

sealed interface PlanificadorEvent {
    data object GetPlanificador : PlanificadorEvent
}