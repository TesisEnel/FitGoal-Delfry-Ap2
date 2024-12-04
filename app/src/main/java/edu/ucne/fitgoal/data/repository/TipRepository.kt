package edu.ucne.fitgoal.data.repository

import edu.ucne.fitgoal.data.local.dao.TipDao
import edu.ucne.fitgoal.data.local.entities.TipEntity
import edu.ucne.fitgoal.data.remote.RemoteDataSource
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class TipRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val tipDao: TipDao
) {
    fun getTips(): Flow<Resource<List<TipEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val tipsDto = remoteDataSource.getTips()
            val tipsEntity = tipsDto.map { it.toEntity() }
            tipDao.insertTips(tipsEntity)
            emit(Resource.Success(tipsEntity))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexion $errorMessage"))
        } catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
        val tips = tipDao.getAllTips()
        tips.collect{ tips ->
            if (tips.isNotEmpty()) {
                emit(Resource.Success(tips))
            } else {
                emit(Resource.Error("No hay tips"))
            }
        }
    }
}