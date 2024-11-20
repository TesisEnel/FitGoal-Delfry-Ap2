package edu.ucne.fitgoal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.fitgoal.data.local.entities.EjercicioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EjercicioDao {

    @Upsert
    suspend fun save(ejercicio: EjercicioEntity)

    @Upsert
    suspend fun save(ejercicios: List<EjercicioEntity>)

    @Query(
        """
        SELECT * 
        FROM Ejercicios
        WHERE ejercicioId = :id
        LIMIT 1
        """
    )
    suspend fun find(id: Int): EjercicioEntity?

    @Query("SELECT * FROM Ejercicios")
    fun getAll(): Flow<List<EjercicioEntity>>

    @Delete
    suspend fun delete(ejercicio: EjercicioEntity)
}
