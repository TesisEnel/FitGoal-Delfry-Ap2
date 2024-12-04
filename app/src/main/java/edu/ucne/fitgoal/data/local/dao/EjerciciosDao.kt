package edu.ucne.fitgoal.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import edu.ucne.fitgoal.data.local.entities.EjerciciosEntity

@Dao
interface EjerciciosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEjercicio(ejercicio: EjerciciosEntity)

    @Update
    suspend fun updateEjercicio(ejercicio: EjerciciosEntity)

    @Query("UPDATE Ejercicio SET repeticiones = :repeticiones, series = :series WHERE ejercicioId = :ejercicioId")
    suspend fun updateRepsAndSeries(ejercicioId: Int, repeticiones: Int, series: Int): Int


    @Query("SELECT * FROM Ejercicio WHERE ejercicioId = :id")
    suspend fun getEjercicioById(id: Int): EjerciciosEntity?
}