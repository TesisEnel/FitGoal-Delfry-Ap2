package edu.ucne.fitgoal.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.fitgoal.data.local.dao.FitGoalDao
import edu.ucne.fitgoal.data.local.entities.FitGoalEntity

@Database(
    entities = [
        FitGoalEntity::class,
               ],
    version = 1,
    exportSchema = false
)
abstract class FitGoalDb : RoomDatabase(){
    abstract fun fitGoalDao(): FitGoalDao
}