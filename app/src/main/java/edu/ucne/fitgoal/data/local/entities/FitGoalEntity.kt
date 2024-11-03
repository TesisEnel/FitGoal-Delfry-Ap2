package edu.ucne.fitgoal.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FitGoal")
data class FitGoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val usuario: String,
    val fecha: String,
    val hora: String,
    val nombrePlantilla: String = "",
    val estado: String = "",
    val foto: ByteArray,
    val idPlantilla: Int,
    val peso: Float,
    val sets: Int,
    val altura: Float,
    val repeticiones: Int,
    )