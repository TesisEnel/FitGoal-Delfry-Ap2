package edu.ucne.fitgoal.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import edu.ucne.fitgoal.data.local.database.FitGoalDb

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideFitGoalDb(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            FitGoalDb::class.java,
            "FitGoal.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideFitGoalDao(fitGoalDb: FitGoalDb) = fitGoalDb.fitGoalDao()
}