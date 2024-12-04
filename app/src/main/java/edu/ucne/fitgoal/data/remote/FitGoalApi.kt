package edu.ucne.fitgoal.data.remote

import edu.ucne.fitgoal.data.remote.dto.CalendarioDto
import edu.ucne.fitgoal.data.remote.dto.EjercicioDto
import edu.ucne.fitgoal.data.remote.dto.HorarioBebidaDto
import edu.ucne.fitgoal.data.remote.dto.PerfilDto
import edu.ucne.fitgoal.data.remote.dto.PlantillaDto
import edu.ucne.fitgoal.data.remote.dto.PlanificadorDto
import edu.ucne.fitgoal.data.remote.dto.ProgresoUsuarioDto
import edu.ucne.fitgoal.data.remote.dto.RelojDto
import edu.ucne.fitgoal.data.remote.dto.TipDto
import edu.ucne.fitgoal.data.remote.dto.UsuarioDto
import edu.ucne.fitgoal.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FitGoalApi {
    @Headers("X-API-Key:${API_KEY}")
    @GET("api/FitGoal/GetPerfil")
    suspend fun getPerfil(): List<PerfilDto>

    @Headers("X-API-Key:${API_KEY}")
    @POST("api/Usuarios")
    suspend fun postUsuario(@Body usuarioDto: UsuarioDto): UsuarioDto

    @Headers("X-API-Key:${API_KEY}")
    @PUT("api/Usuarios/{id}")
    suspend fun putUsuario(@Path("id") id: String, @Body usuarioDto: UsuarioDto): Response<Unit>

    @Headers("X-API-Key:${API_KEY}")
    @GET("api/Usuarios/{id}")
    suspend fun getUsuario(@Path("id") id: String): UsuarioDto

    @Headers("X-API-Key:${API_KEY}")
    @GET("api/Ejercicios")
    suspend fun getEjercicios(): List<EjercicioDto>

    @Headers("X-API-Key:${API_KEY}")
    @GET("api/HorarioBebidas/List/{id}")
    suspend fun getHorarioBebidas(@Path("id") id: String): List<HorarioBebidaDto>

    @Headers("X-API-Key:${API_KEY}")
    @GET("api/HorarioBebidas/{id}")
    suspend fun getHorarioBebida(@Path("id") id: Int): HorarioBebidaDto

    @Headers("X-API-Key:${API_KEY}")
    @PUT("api/HorarioBebidas/{id}")
    suspend fun putHorarioBebida(@Path("id") id: Int, @Body horarioBebidaDto: HorarioBebidaDto): Response<Unit>

    @Headers("X-API-Key:${API_KEY}")
    @DELETE("api/HorarioBebidas/{id}")
    suspend fun deleteHorarioBebida(@Path("id") id: Int): Response<Unit>

    @Headers("X-API-Key:${API_KEY}")
    @POST("api/HorarioBebidas")
    suspend fun postHorarioBebida(@Body horarioBebidaDto: HorarioBebidaDto): HorarioBebidaDto

    @Headers("X-API-Key:${API_KEY}")
    @GET("api/Tips")
    suspend fun getTips(): List<TipDto>

    @Headers("X-API-Key:${API_KEY}")
    @GET("api/GetCalendario")
    suspend fun getCalendario(): List<CalendarioDto>

    @Headers("X-API-Key:${API_KEY}")
    @GET("api/GetPlantillas")
    suspend fun getPlantillas(): List<PlantillaDto>
  
    @GET("api/ProgresoUsuarios/List/{id}")
    suspend fun getProgresosUsuario(@Path("id") id: String): List<ProgresoUsuarioDto>

    @Headers("X-API-Key:${API_KEY}")
    @POST("api/ProgresoUsuarios")
    suspend fun postProgresoUsuario(@Body progresoUsuarioDto: ProgresoUsuarioDto): ProgresoUsuarioDto

    @Headers("X-API-Key:${API_KEY}")
    @DELETE("api/ProgresoUsuarios/User/{id}")
    suspend fun deleteProgresosUsuarios(@Path("id") id: String): Response<Unit>
}