package edu.ucne.fitgoal.data.remote

import edu.ucne.fitgoal.data.remote.dto.UsuarioDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val fitGoalApi: FitGoalApi
) {
    suspend fun getPerfil() = fitGoalApi.getPerfil()
    suspend fun getReloj() = fitGoalApi.getReloj()
    suspend fun getPlanificador() = fitGoalApi.getPlanificador()

    suspend fun postUsuario(usuarioDto: UsuarioDto) = fitGoalApi.postUsuario(usuarioDto)
    suspend fun putUsuario(id: String, usuarioDto: UsuarioDto) = fitGoalApi.putUsuario(id, usuarioDto)
    suspend fun getUsuario(id: String) = fitGoalApi.getUsuario(id)
}