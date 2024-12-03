package edu.ucne.fitgoal.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class CountdownWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val endTime = inputData.getLong("endTime", System.currentTimeMillis())
        val currentTime = System.currentTimeMillis()

        val remainingTime = ((endTime - currentTime) / 1000).toInt()
        val sharedPreferences = applicationContext.getSharedPreferences("TimerPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt("remainingTime", remainingTime).apply()

        return if (remainingTime > 0) {
            Result.retry()
        } else {
            sharedPreferences.edit().putInt("remainingTime", 0).apply()
            Result.success()
        }
    }
}