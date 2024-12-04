package edu.ucne.fitgoal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.fitgoal.data.local.entities.ProgresoUsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgresoUsuarioDao {

    @Upsert
    suspend fun save(progresoUsuario: ProgresoUsuarioEntity)

    @Upsert
    suspend fun save(progresosUsuario: List<ProgresoUsuarioEntity>)

    @Query(
        """
        SELECT * 
        FROM ProgresoUsuarios
        WHERE progresoId = :id
        LIMIT 1
        """
    )
    suspend fun find(id: Int): ProgresoUsuarioEntity?

    @Query("SELECT * FROM ProgresoUsuarios WHERE usuarioId = :usuarioId")
    fun getAll(usuarioId: String): Flow<List<ProgresoUsuarioEntity>>

    @Delete
    suspend fun delete(progresoUsuario: ProgresoUsuarioEntity)

    @Query(
        """
        DELETE 
        FROM ProgresoUsuarios
        WHERE usuarioId = :id
        """
    )
    suspend fun delete(id: String)
}