package edu.ucne.fitgoal.data.remote.dto

import edu.ucne.fitgoal.data.local.entities.EjercicioEntity

data class PlantillaDto(
    val plantillaId: Int = 0,
    val hora: String = "",
    val estado: String = "Active",
    val nombrePlantilla: String = "",
    val descripcionPlantilla: String = "",
    val ejercicios: List<EjercicioEntity>,
    val duracionTotal: Int = 0,
    val ejercicioId: Int = 0
)
