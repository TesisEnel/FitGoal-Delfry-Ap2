package edu.ucne.fitgoal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.fitgoal.data.local.entities.HorarioBebidaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HorarioBebidaDao {

    @Upsert
    suspend fun save(horarioBebida: HorarioBebidaEntity)

    @Upsert
    suspend fun save(horariosBebidas: List<HorarioBebidaEntity>)

    @Query(
        """
        SELECT * 
        FROM HorarioBebidas
        WHERE horarioBebidaId = :id
        LIMIT 1
        """
    )
    suspend fun find(id: Int): HorarioBebidaEntity?

    @Query("SELECT * FROM HorarioBebidas WHERE usuarioId = :usuarioId")
    fun getAll(usuarioId: String): Flow<List<HorarioBebidaEntity>>

    @Delete
    suspend fun delete(horarioBebida: HorarioBebidaEntity)

    @Delete
    suspend fun delete(horarioBebidas: List<HorarioBebidaEntity>)
}
