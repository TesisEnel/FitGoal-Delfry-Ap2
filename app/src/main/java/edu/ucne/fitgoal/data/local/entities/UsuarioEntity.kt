package edu.ucne.fitgoal.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "Usuarios")
data class UsuarioEntity(
    @PrimaryKey
    val usuarioId: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val edad: Int = 0,
    val altura: Float = 0.0f,
    val pesoActual: Float = 0.0f,
    val pesoIdeal: Float = 0.0f,
    val aguaDiaria: Float = 0.0f
)

