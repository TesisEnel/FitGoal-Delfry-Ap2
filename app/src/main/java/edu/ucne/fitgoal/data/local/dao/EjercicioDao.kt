package edu.ucne.fitgoal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEjercicio(ejercicio: EjercicioEntity)

    @Update
    suspend fun updateEjercicio(ejercicio: EjercicioEntity)

    @Query("UPDATE Ejercicios SET repeticiones = :repeticiones, series = :series WHERE ejercicioId = :ejercicioId")
    suspend fun updateRepsAndSeries(ejercicioId: Int, repeticiones: Int, series: Int): Int


    @Query("SELECT * FROM Ejercicios WHERE ejercicioId = :id")
    suspend fun getEjercicioById(id: Int): EjercicioEntity?

}

