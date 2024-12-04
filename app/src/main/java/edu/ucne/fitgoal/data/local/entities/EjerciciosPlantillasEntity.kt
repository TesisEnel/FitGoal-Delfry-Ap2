package edu.ucne.fitgoal.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ejercicios_plantillas")
data class EjerciciosPlantillasEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val plantillaId: Int,
    val ejercicioId: Int,
)