package edu.ucne.fitgoal.data.repository

import edu.ucne.fitgoal.data.local.dao.HorarioBebidaDao
import edu.ucne.fitgoal.data.local.entities.HorarioBebidaEntity
import edu.ucne.fitgoal.data.remote.RemoteDataSource
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.HorarioBebidaDto
import edu.ucne.fitgoal.data.remote.dto.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class HoraioBebidaRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val horarioBebidaDao: HorarioBebidaDao
) {
    fun getHorarioBebidas(id: String): Flow<Resource<List<HorarioBebidaEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val horarioBebidas = remoteDataSource.getHorarioBebidas(id)
            val horarioBebidasEntities = horarioBebidas.map { it.toEntity() }
            horarioBebidaDao.save(horarioBebidasEntities)
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexion $errorMessage"))
        } catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
        val horarioBebidas = horarioBebidaDao.getAll(id)
        horarioBebidas.collect { horarios ->
            emit(Resource.Success(horarios))
        }
    }

    fun putHorarioBebida(id: Int, horarioBebidaDto: HorarioBebidaDto): Flow<Resource<HorarioBebidaEntity>> = flow {
        try {
            emit(Resource.Loading())
            remoteDataSource.putHorarioBebida(id, horarioBebidaDto)
            horarioBebidaDao.save(horarioBebidaDto.toEntity())
            emit(Resource.Success(horarioBebidaDto.toEntity()))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexion $errorMessage"))
        }catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
    }

    suspend fun deleteHorarioBebida(id: Int): Resource<Unit> {
        return try {
            remoteDataSource.deleteHorarioBebida(id)
            val horarioBebida = horarioBebidaDao.find(id)
            if (horarioBebida != null) {
                horarioBebidaDao.delete(horarioBebida)
            }
            Resource.Success(Unit)
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            Resource.Error("Error de conexion $errorMessage")
        } catch (e: Exception) {
            val horarioBebida = horarioBebidaDao.find(id)
            if (horarioBebida != null) {
                horarioBebidaDao.delete(horarioBebida)
            }
            Resource.Success(Unit)
        }
    }

    fun postHorarioBebida(horarioBebidaDto: HorarioBebidaDto): Flow<Resource<HorarioBebidaEntity>> =
        flow {
            try {
                emit(Resource.Loading())
                val horarioBebida = remoteDataSource.postHorarioBebida(horarioBebidaDto)
                val horarioEntity = horarioBebida.toEntity()
                horarioBebidaDao.save(horarioEntity)
                emit(Resource.Success(horarioEntity))
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
                emit(Resource.Error("Error de conexion $errorMessage"))
            } catch (e: Exception) {
                emit(Resource.Error("Error ${e.message}"))
            }
        }

    fun getHorarioBebida(id: Int): Flow<Resource<HorarioBebidaEntity>> = flow {
        try {
            emit(Resource.Loading())
            val horarioBebida = remoteDataSource.getHorarioBebida(id)
            val horarioBebidaEntity = horarioBebida.toEntity()
            horarioBebidaDao.save(horarioBebidaEntity)
            val horarioBebidaLocal = horarioBebidaDao.find(id)
            if (horarioBebidaLocal != null) {
                emit(Resource.Success(horarioBebidaLocal))
            } else {
                emit(Resource.Success(horarioBebidaEntity))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
    }

    fun getHorarioBebidasLocal(id: String) = horarioBebidaDao.getAll(id)
}