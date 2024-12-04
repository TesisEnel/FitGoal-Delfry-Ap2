package edu.ucne.fitgoal.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import edu.ucne.fitgoal.data.local.database.Converters

@TypeConverters(Converters::class)
@Entity(tableName = "Calendario")
data class CalendarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val usuarioId: Int,
    val nombreEvento: String = "",
    val selectedDates: List<String>,
    val fecha: String = "",
    val hora: String = "",
    val estado: String = "",
)