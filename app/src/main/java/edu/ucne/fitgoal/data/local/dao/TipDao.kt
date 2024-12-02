package edu.ucne.fitgoal.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import edu.ucne.fitgoal.data.local.entities.TipEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TipDao {
    @Query("SELECT * FROM Tips")
    fun getAllTips(): Flow<List<TipEntity>>

    @Insert
    suspend fun insertTips(tips: List<TipEntity>)
}