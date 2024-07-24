package com.perfomax.flexstats.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.perfomax.flexstats.data_api.repository.StatsRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val WORKER_TAG = "STATS.UPDATE.WORKER"

class StatsUpdateWorker (
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @Inject
    lateinit var repository: StatsRepository

    override suspend fun doWork(): Result {
        Log.d("MyLog", "Start StatsUpdateWorker")
        repository.updateStats()
        return Result.success()
    }

    companion object {

        private val workerRequest : PeriodicWorkRequest.Builder
            get() {
                return PeriodicWorkRequestBuilder<StatsUpdateWorker>(15, TimeUnit.MINUTES)
                    .addTag(WORKER_TAG)
            }

        fun enqueue(workManager: WorkManager) {
            workManager.enqueue(workerRequest.build())
        }
    }
}