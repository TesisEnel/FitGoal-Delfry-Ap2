package edu.ucne.fitgoal.data.remote

import edu.ucne.fitgoal.data.remote.dto.PerfilDto
import edu.ucne.fitgoal.data.remote.dto.PlanificadorDto
import edu.ucne.fitgoal.data.remote.dto.RelojDto
import retrofit2.http.GET
import retrofit2.http.Headers

interface FitGoalApi {
    @Headers("X-API-Key:test")
    @GET("api/FitGoal/GetPerfil")
    suspend fun getPerfil(): List<PerfilDto>

    @Headers("X-API-Key:test")
    @GET("api/FitGoal/GetClientes")
    suspend fun getReloj(): List<RelojDto>

    @Headers("X-API-Key:test")
    @GET("api/FitGoal/GetCalendario")
    suspend fun getPlanificador(): List<PlanificadorDto>
}