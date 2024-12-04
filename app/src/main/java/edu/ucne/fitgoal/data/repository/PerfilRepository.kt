package edu.ucne.fitgoal.data.repository

import edu.ucne.fitgoal.data.local.dao.UsuarioDao
import edu.ucne.fitgoal.data.local.entities.UsuarioEntity
import edu.ucne.fitgoal.data.remote.RemoteDataSource
import edu.ucne.fitgoal.data.remote.Resource
import edu.ucne.fitgoal.data.remote.dto.UsuarioDto
import edu.ucne.fitgoal.data.remote.dto.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class PerfilRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val usuarioDao: UsuarioDao
) {
    fun getUsuario(id: String): Flow<Resource<UsuarioEntity>> = flow {
        try {
            emit(Resource.Loading())
            val usuarioDto = remoteDataSource.getUsuario(id)
            val usuarioEntity = usuarioDto.toEntity()
            usuarioDao.save(usuarioEntity)
            emit(Resource.Success(usuarioEntity))
        } catch (e: HttpException) {
            val usuarioLocal = usuarioDao.getUsuarioById(id).firstOrNull()
            if (usuarioLocal != null) {
                emit(Resource.Success(usuarioLocal))
            } else {
                emit(Resource.Error("No se encontraron datos locales"))
            }
        } catch (e: Exception) {
            val usuarioLocal = usuarioDao.getUsuarioById(id).firstOrNull()
            if (usuarioLocal != null) {
                emit(Resource.Success(usuarioLocal))
            } else {
                emit(Resource.Error("Error: ${e.message}"))
            }
        }
    }

    fun updateUsuario(id: String, usuarioDto: UsuarioDto): Flow<Resource<UsuarioEntity>> = flow {
        try {
            emit(Resource.Loading())
            remoteDataSource.putUsuario(id, usuarioDto)
            usuarioDao.save(usuarioDto.toEntity())
            emit(Resource.Success(usuarioDto.toEntity()))
        } catch (e: HttpException) {
            emit(Resource.Error("Error al actualizar el usuario: ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido: ${e.message}"))
        }
    }
}