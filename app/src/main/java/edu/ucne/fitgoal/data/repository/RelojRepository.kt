package edu.ucne.fitgoal.data.repository

import edu.ucne.fitgoal.data.remote.RemoteDataSource
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.RelojDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class RelojRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
){
    fun getReloj(): Flow<Resource<List<RelojDto>>> = flow {
        try {
            emit(Resource.Loading())
            val relojes = remoteDataSource.getReloj()
            emit(Resource.Success(relojes))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexion $errorMessage"))
        } catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
    }

}