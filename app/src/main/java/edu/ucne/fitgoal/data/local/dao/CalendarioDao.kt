package edu.ucne.fitgoal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.fitgoal.data.local.entities.CalendarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarioDao {
    @Upsert
    suspend fun save(calendario: CalendarioEntity)

    @Query("SELECT * FROM Calendario WHERE id = :id LIMIT 1")
    suspend fun find(id: Int): CalendarioEntity?

    @Upsert
    suspend fun update(calendario: CalendarioEntity)

    @Query("SELECT * FROM Calendario")
    fun getAll(): Flow<List<CalendarioEntity>>

    @Query("SELECT * FROM Calendario LIMIT 1")
    suspend fun getFirst(): CalendarioEntity?
}