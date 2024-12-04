package edu.ucne.fitgoal.data.remote.dto

import edu.ucne.fitgoal.data.local.entities.ProgresoUsuarioEntity

data class ProgresoUsuarioDto(
    val progresoId: Int = 0,
    val usuarioId: String = "",
    val fecha : String = "",
    val peso: Float = 0f,
)

fun ProgresoUsuarioDto.toEntity() = ProgresoUsuarioEntity(
    progresoId = this.progresoId,
    usuarioId = this.usuarioId,
    fecha = this.fecha,
    peso = this.peso
)
