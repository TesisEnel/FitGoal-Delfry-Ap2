package edu.ucne.fitgoal.presentation.aguadiaria

import edu.ucne.fitgoal.data.local.entities.HorarioBebidaEntity
import edu.ucne.fitgoal.data.remote.dto.HorarioBebidaDto

data class UiState(
    val isLoading: Boolean = false,
    val error: String = "",
    val isModalVisible : Boolean = false,
    val horarios : List<HorarioBebidaEntity> = emptyList(),
    val cantidad: Float = 0f,
    val cantidadError: String = "",
    val hora: String = "",
    val horaError: String = "",
    val horarioBebidaId: Int = 0,
    val usuarioId: String = "",
    val update: Boolean = false,
    val canDelete: Boolean = true
)

fun UiState.toDto() = HorarioBebidaDto(
    horarioBebidaId = horarioBebidaId,
    cantidad = cantidad,
    hora = hora,
    usuarioId = usuarioId
)