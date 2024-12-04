package edu.ucne.fitgoal.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ejercicios")
data class EjercicioEntity(
    @PrimaryKey
    val ejercicioId: Int = 0,
    val nombreEjercicio: String? = null,
    val descripcion: String? = null,
    val foto: String? = null,
    val grupoMuscular: String? = null,
    val repeticiones: Int? = null,
    val series: Int? = null,
    val duracionEjercicio: String? = null,
    val plantillaId: Int? = null,
)

