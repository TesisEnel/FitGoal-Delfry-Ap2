package edu.ucne.fitgoal.data.remote.dto

import androidx.room.PrimaryKey

data class EjerciciosDto(
    @PrimaryKey(autoGenerate = true)
    val ejercicioId: Int,
    val nombreEjercicio: String = "",
    val descripcion: String = "",
    val foto: String = "",
    val grupoMuscular: String = "",
    val plantillaId: Int = 0,
    var repeticiones: Int = 0,
    var series: Int = 0,
    var duracionEjercicio: Int = 0
)