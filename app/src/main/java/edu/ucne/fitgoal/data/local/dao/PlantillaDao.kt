package edu.ucne.fitgoal.data.local.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.Query
import androidx.room.Relation
import edu.ucne.fitgoal.data.local.entities.PlantillaEntity
import androidx.room.Transaction
import androidx.room.Update
import edu.ucne.fitgoal.data.local.entities.EjercicioEntity
import edu.ucne.fitgoal.data.local.entities.EjerciciosPlantillasEntity
import edu.ucne.fitgoal.data.remote.dto.EjerciciosDto

@Dao
interface PlantillaDao {

    @Query("DELETE FROM ejercicios_plantillas WHERE plantillaId = :plantillaId")
    suspend fun deleteEjerciciosFromPlantilla(plantillaId: Int)

    @Query("DELETE FROM Plantilla WHERE plantillaId = :plantillaId")
    suspend fun deletePlantillaById(plantillaId: Int)

    @Query("DELETE FROM ejercicios_plantillas WHERE plantillaId = :plantillaId")
    suspend fun deleteEjerciciosByPlantillaId(plantillaId: Int)


    @Query("SELECT * FROM Plantilla")
    suspend fun getAllPlantillas(): List<PlantillaEntity>

    @Update
    suspend fun actualizarPlantilla(plantilla: PlantillaEntity)

    @Insert
    suspend fun insertPlantilla(plantilla: PlantillaEntity): Long

    @Insert
    suspend fun insertEjercicios(ejercicios: List<EjercicioEntity>)

    @Insert
    suspend fun insertEjerciciosPlantillas(relaciones: List<EjerciciosPlantillasEntity>)


    @Transaction
    @Query("SELECT * FROM Plantilla WHERE plantillaId = :plantillaId")
    suspend fun getEjerciciosByPlantillaId(plantillaId: Int): List<EjercicioEntity> {
        val plantillaWithEjercicios = getPlantillaWithEjercicios(plantillaId)
        return plantillaWithEjercicios.ejercicios
    }
    @Transaction
    @Insert
    suspend fun insertEjerciciosPlantillasWithDetails(
        plantillaId: Int,
        ejercicios: List<EjerciciosDto>
    ) {
        val relaciones = ejercicios.map {
            EjerciciosPlantillasEntity(
                plantillaId = plantillaId,
                ejercicioId = it.ejercicioId
            )
        }
        val ejerciciosEntities = ejercicios.map {
            EjercicioEntity(
                ejercicioId = it.ejercicioId,
                nombreEjercicio = it.nombreEjercicio,
                foto = it.foto,
                repeticiones = it.repeticiones,
                series = it.series,
                plantillaId = plantillaId,
                descripcion = it.descripcion,
                grupoMuscular = it.grupoMuscular,
                duracionEjercicio = it.duracionEjercicio.toString()
            )
        }
        insertEjercicios(ejerciciosEntities)
        insertEjerciciosPlantillas(relaciones)
    }

    @Transaction
    @Query("SELECT * FROM Plantilla WHERE plantillaId = :id")
    suspend fun getPlantillaWithEjercicios(id: Int): PlantillaWithEjercicios
}

data class PlantillaWithEjercicios(
    @Embedded val plantilla: PlantillaEntity,
    @Relation(
        parentColumn = "plantillaId",
        entityColumn = "ejercicioId",
        associateBy = Junction(EjerciciosPlantillasEntity::class)
    )
    val ejercicios: List<EjercicioEntity>
)