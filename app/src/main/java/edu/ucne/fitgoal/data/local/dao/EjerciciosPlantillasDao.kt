package edu.ucne.fitgoal.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.ucne.fitgoal.data.local.entities.EjerciciosPlantillasEntity

@Dao
interface EjerciciosPlantillasDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ejercicioPlantilla: EjerciciosPlantillasEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ejerciciosPlantillas: List<EjerciciosPlantillasEntity>)

    @Query("DELETE FROM ejercicios_plantillas WHERE ejercicioId = :id")
    suspend fun deleteById(id: Int)
}