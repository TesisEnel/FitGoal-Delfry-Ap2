package edu.ucne.fitgoal.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ProgresoUsuarios")
data class ProgresoUsuarioEntity(
    @PrimaryKey
    val progresoId: Int? = 0,
    val usuarioId: String? = null,
    val fecha : String? = null,
    val peso: Float? = 0f,
)
