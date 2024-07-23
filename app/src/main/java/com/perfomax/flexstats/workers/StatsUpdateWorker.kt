package com.perfomax.flexstats.workers

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

private const val WORKER_TAG = "STATS.UPDATE.WORKER"

class StatsUpdateWorker (
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {

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