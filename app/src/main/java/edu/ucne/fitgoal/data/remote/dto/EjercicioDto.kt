package edu.ucne.fitgoal.data.remote.dto

import edu.ucne.fitgoal.data.local.entities.EjercicioEntity

data class EjercicioDto(
    val ejercicioId: Int?,
    val nombreEjercicio: String = "",
    val descripcion: String = "",
    val foto: String = "",
    val grupoMuscular: String = "",
    val repeticiones: Int = 0,
    val series: Int = 0,
    val duracionEjercicio: String = "",
    val plantillaId: Int? = null
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

fun EjercicioEntity.toDto(): EjercicioDto {
    return EjercicioDto(
        ejercicioId = this.ejercicioId,
        nombreEjercicio = this.nombreEjercicio ?: "",
        descripcion = this.descripcion ?: "",
        foto = this.foto ?: "",
        grupoMuscular = this.grupoMuscular ?: "",
        repeticiones = this.repeticiones ?: 0,
        series = this.series ?: 0
    )
}
