package edu.ucne.fitgoal.data.repository

import edu.ucne.fitgoal.data.remote.RemoteDataSource
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.UsuarioDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class PerfilRepository @Inject constructor (
    private val remoteDataSource: RemoteDataSource
){
    fun getUsuario(id: String): Flow<Resource<UsuarioDto>> = flow {
        try {
            emit(Resource.Loading())
            val usuario = remoteDataSource.getUsuario(id)
            emit(Resource.Success(usuario))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexión: $errorMessage"))
        } catch (e: Exception) {
            emit(Resource.Error("Error: ${e.message}"))
        }
    }
}