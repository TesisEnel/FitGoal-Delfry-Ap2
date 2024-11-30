package edu.ucne.fitgoal.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HorarioBebidas")
data class HorarioBebidaEntity(
    @PrimaryKey
    val horarioBebidaId: Int? = null,
    val usuarioId: String = "",
    val cantidad: Float = 0f,
    val hora: String = ""
)