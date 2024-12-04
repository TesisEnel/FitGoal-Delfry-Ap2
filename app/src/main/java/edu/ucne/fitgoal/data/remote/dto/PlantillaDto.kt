package edu.ucne.fitgoal.data.remote.dto

import androidx.room.PrimaryKey
import edu.ucne.fitgoal.data.local.entities.EjercicioEntity


data class PlantillaDto(
    @PrimaryKey(autoGenerate = true)
    val plantillaId: Int = 0,
    val hora: String = "",
    val estado: String = "Active",
    val nombrePlantilla: String = "",
    val descripcionPlantilla: String = "",
    val ejercicios: List<EjercicioEntity>,
    val duracionTotal: Int = 0,
    val ejercicioId: Int = 0
)
