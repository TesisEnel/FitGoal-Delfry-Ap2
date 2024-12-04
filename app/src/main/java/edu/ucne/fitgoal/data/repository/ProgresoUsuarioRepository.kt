package edu.ucne.fitgoal.data.repository

import edu.ucne.fitgoal.data.local.dao.ProgresoUsuarioDao
import edu.ucne.fitgoal.data.local.entities.ProgresoUsuarioEntity
import edu.ucne.fitgoal.data.remote.RemoteDataSource
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.ProgresoUsuarioDto
import edu.ucne.fitgoal.data.remote.dto.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import retrofit2.HttpException
import javax.inject.Inject

class ProgresoUsuarioRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val progresoUsuarioDao: ProgresoUsuarioDao
) {

    fun getProgresosUsuario(usuarioId: String): Flow<Resource<List<ProgresoUsuarioEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val progresosUsuario = remoteDataSource.getProgresoUsuario(usuarioId)
            val progresosUsuarioEntities = progresosUsuario.map { it.toEntity() }
            progresoUsuarioDao.save(progresosUsuarioEntities)
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexion $errorMessage"))
        } catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
        val progresosUsuario = progresoUsuarioDao.getAll(usuarioId)
        progresosUsuario.collect { progresos ->
            if(progresos.isNotEmpty()){
                emit(Resource.Success(progresos))
            }
        }
    }

    fun deleteProgresosUsuario(usuarioId: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            remoteDataSource.deleteProgresosUsuarios(usuarioId)
            progresoUsuarioDao.delete(usuarioId)
            emit(Resource.Success(true))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexion $errorMessage"))
        } catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
    }

    fun createProgresoUsuario(progresoUsuario: ProgresoUsuarioDto): Flow<Resource<ProgresoUsuarioEntity>> = flow {
        try {
            emit(Resource.Loading())
            val result = remoteDataSource.postProgresoUsuario(progresoUsuario)
            progresoUsuarioDao.save(result.toEntity())
            emit(Resource.Success(result.toEntity()))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexion $errorMessage"))
        } catch (e: Exception) {
            emit(Resource.Error("Error ${e.message}"))
        }
    }
}