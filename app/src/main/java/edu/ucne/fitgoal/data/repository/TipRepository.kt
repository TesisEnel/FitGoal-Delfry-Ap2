package edu.ucne.fitgoal.data.repository

import edu.ucne.fitgoal.data.local.dao.TipDao
import edu.ucne.fitgoal.data.local.entities.TipEntity
import edu.ucne.fitgoal.data.remote.RemoteDataSource
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.TipDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
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
            val tipsDto: List<TipDto> = remoteDataSource.getTips()
            val tipsEntity = tipsDto.map { dto ->
                TipEntity(
                    tipId = dto.tipId,
                    nombre = dto.nombre,
                    descripcion = dto.descripcion
                )
            }

            tipDao.insertTips(tipsEntity)
            emit(Resource.Success(tipsEntity))
        } catch (e: HttpException) {
            val localTips = tipDao.getAllTips().firstOrNull()
            if (localTips != null) {
                emit(Resource.Success(localTips))
            } else {
                emit(Resource.Error("No se encontraron datos locales"))
            }
        } catch (e: Exception) {
            val localTips = tipDao.getAllTips().firstOrNull()
            if (localTips != null) {
                emit(Resource.Success(localTips))
            } else {
                emit(Resource.Error("Error: ${e.message}"))
            }
        }
    }
}