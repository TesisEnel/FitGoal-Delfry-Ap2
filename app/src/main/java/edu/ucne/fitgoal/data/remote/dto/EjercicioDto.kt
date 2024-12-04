package edu.ucne.fitgoal.data.remote.dto

import edu.ucne.fitgoal.data.local.entities.EjercicioEntity

data class EjercicioDto(
    val ejercicioId: Int?,
    val nombreEjercicio: String = "",
    val descripcion: String = "",
    val foto: String = "",
    val grupoMuscular: String = "",
    val repeticiones: Int = 0,
    val series: Int = 0
)

fun EjercicioDto.toEntity(): EjercicioEntity {
    return EjercicioEntity(
        ejercicioId = this.ejercicioId ?: 0,
        nombreEjercicio = this.nombreEjercicio,
        descripcion = this.descripcion,
        foto = this.foto,
        grupoMuscular = this.grupoMuscular,
        repeticiones = this.repeticiones,
        series = this.series
    )
}
