package edu.ucne.fitgoal.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.fitgoal.data.local.dao.FitGoalDao
import edu.ucne.fitgoal.data.local.dao.UsuarioDao
import edu.ucne.fitgoal.data.local.entities.FitGoalEntity
import edu.ucne.fitgoal.data.local.entities.UsuarioEntity

@Database(
    entities = [
        FitGoalEntity::class,
        UsuarioEntity::class
               ],
    version = 2,
    exportSchema = false
)
abstract class FitGoalDb : RoomDatabase(){
    abstract fun fitGoalDao(): FitGoalDao
    abstract fun usuarioDao(): UsuarioDao
}