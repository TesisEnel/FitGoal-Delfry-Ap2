package edu.ucne.fitgoal.data.remote

import edu.ucne.fitgoal.data.remote.dto.HorarioBebidaDto
import edu.ucne.fitgoal.data.remote.dto.TipDto
import edu.ucne.fitgoal.data.remote.dto.UsuarioDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val fitGoalApi: FitGoalApi
) {
    suspend fun getPerfil() = fitGoalApi.getPerfil()
    suspend fun getReloj() = fitGoalApi.getReloj()
    suspend fun getPlantillas() = fitGoalApi.getPlantillas()
    suspend fun getCalendario() = fitGoalApi.getCalendario()

    suspend fun postUsuario(usuarioDto: UsuarioDto) = fitGoalApi.postUsuario(usuarioDto)
    suspend fun putUsuario(id: String, usuarioDto: UsuarioDto) = fitGoalApi.putUsuario(id, usuarioDto)
    suspend fun getUsuario(id: String) = fitGoalApi.getUsuario(id)

    suspend fun getEjercicios() = fitGoalApi.getEjercicios()

    suspend fun getHorarioBebidas(id: String) = fitGoalApi.getHorarioBebidas(id)
    suspend fun getHorarioBebida(id: Int) = fitGoalApi.getHorarioBebida(id)
    suspend fun putHorarioBebida(id: Int, horarioBebidaDto: HorarioBebidaDto) = fitGoalApi.putHorarioBebida(id, horarioBebidaDto)
    suspend fun deleteHorarioBebida(id: Int) = fitGoalApi.deleteHorarioBebida(id)
    suspend fun postHorarioBebida(horarioBebidaDto: HorarioBebidaDto) = fitGoalApi.postHorarioBebida(horarioBebidaDto)

    suspend fun getTips(): List<TipDto> = fitGoalApi.getTips()

}