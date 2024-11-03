package edu.ucne.fitgoal.data.repository

import edu.ucne.fitgoal.data.remote.RemoteDataSource
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.PerfilDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import retrofit2.HttpException

class PerfilRepository @Inject constructor (
    private val remoteDataSource: RemoteDataSource
){
    fun getPerfil(): Flow<Resource<List<PerfilDto>>> = flow {
        try {
            emit(Resource.Loading())
            val clientes = remoteDataSource.getPerfil()
            emit(Resource.Success(clientes))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexion $errorMessage"))
        } catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
    }
}