package edu.ucne.fitgoal.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Plantilla")
data class PlantillaEntity(
    @PrimaryKey(autoGenerate = true)
    val plantillaId: Int = 0,

    @ColumnInfo(name = "nombre")
    val nombre: String,

    @ColumnInfo(name = "descripcion")
    val descripcion: String,

    val ejercicios: List<EjerciciosEntity>,
    val duracionTotal: String = ""
)