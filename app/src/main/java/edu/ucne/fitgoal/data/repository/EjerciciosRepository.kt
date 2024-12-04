package edu.ucne.fitgoal.data.repository

import edu.ucne.fitgoal.data.local.dao.EjerciciosDao
import edu.ucne.fitgoal.data.local.entities.EjerciciosEntity
import edu.ucne.fitgoal.data.remote.FitGoalApi
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.EjerciciosDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class EjerciciosRepository @Inject constructor(
    private val api: FitGoalApi,
    private val EjerciciosDao: EjerciciosDao
) {
    fun getEjercicios(): Flow<Resource<List<EjerciciosDto>>> = flow {
        emit(Resource.Loading())

        try {
            val response = api.getEjercicios()
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            val errorMessage = "Error HTTP: ${e.code()} - ${e.message()}"
            emit(Resource.Error(errorMessage))
        } catch (e: IOException) {
            emit(Resource.Error("Error de conexión: ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido: ${e.message ?: e.toString()}"))
        }
    }

    suspend fun updateEjercicio(ejercicio: EjerciciosEntity) {
        EjerciciosDao.updateEjercicio(ejercicio)
    }
    suspend fun updateRepsAndSeries(ejercicioId: Int, repeticiones: Int, series: Int): Int {
        return EjerciciosDao.updateRepsAndSeries(ejercicioId, repeticiones, series)
    }
}