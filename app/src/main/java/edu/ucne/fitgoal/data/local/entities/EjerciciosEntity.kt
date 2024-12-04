package edu.ucne.fitgoal.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ejercicio")
data class EjerciciosEntity(
    @PrimaryKey(autoGenerate = true)
    val ejercicioId: Int,
    val nombreEjercicio: String,
    val descripcion: String,
    val foto: String,
    val grupoMuscular: String,
    val plantillaId: Int,
    var repeticiones: Int,
    var series: Int,
    var duracionEjercicio: Int
)