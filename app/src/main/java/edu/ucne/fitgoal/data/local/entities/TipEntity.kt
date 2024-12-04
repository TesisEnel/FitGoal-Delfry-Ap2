package edu.ucne.fitgoal.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tips")
data class TipEntity(
    @PrimaryKey
    val tipId: Int,
    val nombre: String?,
    val descripcion: String?
)