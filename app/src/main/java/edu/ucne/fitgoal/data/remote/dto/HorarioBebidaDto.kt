package edu.ucne.fitgoal.data.remote.dto

import edu.ucne.fitgoal.data.local.entities.HorarioBebidaEntity

data class HorarioBebidaDto(
    var horarioBebidaId: Int =0,
    val usuarioId: String = "",
    val cantidad: Float = 0f,
    val hora: String = ""
)

fun HorarioBebidaDto.toEntity(): HorarioBebidaEntity {
    return HorarioBebidaEntity(
        horarioBebidaId = this.horarioBebidaId,
        usuarioId = this.usuarioId,
        cantidad = this.cantidad,
        hora = this.hora
    )
}
