package edu.ucne.fitgoal.data.repository

import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.CalendarioDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject
import edu.ucne.fitgoal.data.remote.RemoteDataSource

class CalendarioRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
){
    fun getCalendario(): Flow<Resource<List<CalendarioDto>>> = flow {
        try {
            emit(Resource.Loading())
            val calendario = remoteDataSource.getCalendario()
            emit(Resource.Success(calendario))
            } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexion $errorMessage"))
            } catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
    }
}