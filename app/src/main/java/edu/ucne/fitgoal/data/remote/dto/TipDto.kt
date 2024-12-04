package edu.ucne.fitgoal.data.remote.dto

import edu.ucne.fitgoal.data.local.entities.TipEntity

data class TipDto(
    val tipId: Int,
    val nombre: String?,
    val descripcion: String?
)

fun TipDto.toEntity() = TipEntity(
    tipId = this.tipId,
    nombre = this.nombre,
    descripcion = this.descripcion
)