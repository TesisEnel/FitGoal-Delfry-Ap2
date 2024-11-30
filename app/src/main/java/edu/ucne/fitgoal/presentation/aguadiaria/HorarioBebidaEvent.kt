package edu.ucne.fitgoal.presentation.aguadiaria

import android.content.Context

sealed interface HorarioBebidaEvent {
    data class CantidadChange(val cantidad: String) : HorarioBebidaEvent
    data class HoraChange(val hora: String) : HorarioBebidaEvent
    data object SaveHorarioBebida : HorarioBebidaEvent
    data class DeleteHorarioBebida(val id: Int) : HorarioBebidaEvent
    data class SelectHorariosBebida(val id: Int) : HorarioBebidaEvent
    data object CloseModal : HorarioBebidaEvent
    data object CloseModalError : HorarioBebidaEvent
    data object OpenModal : HorarioBebidaEvent
    data class GetHorarioBebidas(val context: Context) : HorarioBebidaEvent
}