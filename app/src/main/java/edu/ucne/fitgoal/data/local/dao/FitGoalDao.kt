package edu.ucne.fitgoal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.fitgoal.data.local.entities.FitGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FitGoalDao{
    @Upsert()
    suspend fun save(fitGoal: FitGoalEntity)
    @Query(
        """
        SELECT * 
        FROM FitGoal 
        WHERE id=:id  
        LIMIT 1
        """
    )
    suspend fun find(id: Int): FitGoalEntity?

    @Delete
    suspend fun delete(fitGoal: FitGoalEntity)

    @Query("SELECT * FROM FitGoal")
    fun getAll(): Flow<List<FitGoalEntity>>
}