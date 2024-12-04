package edu.ucne.fitgoal.data.remote

import edu.ucne.fitgoal.data.remote.dto.CalendarioDto
import edu.ucne.fitgoal.data.remote.dto.EjerciciosDto
import edu.ucne.fitgoal.data.remote.dto.PerfilDto
import edu.ucne.fitgoal.data.remote.dto.PlantillaDto
import edu.ucne.fitgoal.data.remote.dto.RelojDto
import edu.ucne.fitgoal.data.remote.dto.UsuarioDto
import edu.ucne.fitgoal.util.Constants.API_KEY
import retrofit2.http.Body
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
    @GET("api/FitGoal/GetClientes")
    suspend fun getReloj(): List<RelojDto>

    @Headers("X-API-Key:${API_KEY}")
    @GET("api/FitGoal/GetCalendario")
    suspend fun getPlantillas(): List<PlantillaDto>

    @Headers("X-API-Key:${API_KEY}")
    @GET("api/FitGoal/GetCalendario")
    suspend fun getCalendario(): List<CalendarioDto>

    @Headers("X-API-Key:${API_KEY}")
    @POST("api/Usuarios")
    suspend fun postUsuario(@Body usuarioDto: UsuarioDto): UsuarioDto

    @Headers("X-API-Key:${API_KEY}")
    @PUT("api/Usuarios/{id}")
    suspend fun putUsuario(@Path("id") id: String, @Body usuarioDto: UsuarioDto): UsuarioDto

    @Headers("X-API-Key:${API_KEY}")
    @GET("api/Usuarios/{id}")
    suspend fun getUsuario(@Path("id") id: String): UsuarioDto

    @Headers("X-API-Key:${API_KEY}")
    @GET("api/Ejercicios")
    suspend fun getEjercicios(): List<EjerciciosDto>

    @Headers("X-API-Key:${API_KEY}")
    @GET("api/Plantilla/{id}")
    suspend fun getPlantillas(@Path("id") id: Int): PlantillaDto
}