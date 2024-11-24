package edu.ucne.fitgoal.data.repository

import edu.ucne.fitgoal.data.local.dao.EjercicioDao
import edu.ucne.fitgoal.data.local.entities.EjercicioEntity
import edu.ucne.fitgoal.data.remote.RemoteDataSource
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class EjercicioRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val ejercicioDao: EjercicioDao
) {
    fun getEjercicios(): Flow<Resource<List<EjercicioEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val ejercicios = remoteDataSource.getEjercicios()
            val ejerciciosEntities = ejercicios.map { it.toEntity() }
            ejercicioDao.save(ejerciciosEntities)
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexion $errorMessage"))
        }catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
        val ejercicios = ejercicioDao.getAll()
        ejercicios.collect {
            if (it.isNotEmpty()) {
                emit(Resource.Success(it))
            } else {
                emit(Resource.Error("No hay ejercicios"))
            }
        }
    }

    fun getEjerciciosLocales() = ejercicioDao.getAll()
}