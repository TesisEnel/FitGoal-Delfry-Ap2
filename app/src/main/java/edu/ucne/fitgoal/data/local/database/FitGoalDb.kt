package edu.ucne.fitgoal.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.fitgoal.data.local.dao.EjercicioDao
import edu.ucne.fitgoal.data.local.dao.FitGoalDao
import edu.ucne.fitgoal.data.local.dao.HorarioBebidaDao
import edu.ucne.fitgoal.data.local.dao.UsuarioDao
import edu.ucne.fitgoal.data.local.entities.EjercicioEntity
import edu.ucne.fitgoal.data.local.entities.FitGoalEntity
import edu.ucne.fitgoal.data.local.entities.HorarioBebidaEntity
import edu.ucne.fitgoal.data.local.entities.UsuarioEntity

@Database(
    entities = [
        FitGoalEntity::class,
        UsuarioEntity::class,
        EjercicioEntity::class,
        HorarioBebidaEntity::class
               ],
    version = 6,
    exportSchema = false
)
abstract class FitGoalDb : RoomDatabase(){
    abstract fun fitGoalDao(): FitGoalDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun ejercicioDao(): EjercicioDao
    abstract fun horarioBebidaDao(): HorarioBebidaDao
}