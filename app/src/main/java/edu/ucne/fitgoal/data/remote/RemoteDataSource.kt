package edu.ucne.fitgoal.data.remote

class RemoteDataSource(
    private val fitGoalApi: FitGoalApi
) {
    suspend fun getPerfil() = fitGoalApi.getPerfil()
    suspend fun getReloj() = fitGoalApi.getReloj()
    suspend fun getPlanificador() = fitGoalApi.getPlanificador()
}