package com.perfomax.flexstats.home.domain.usecases

import android.content.Context
//import androidx.work.WorkManager
import com.perfomax.flexstats.core.contracts.UseCaseWithoutParams
import com.perfomax.flexstats.data_api.repository.StatsRepository
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class LoadStatsBackgroundUseCase @Inject constructor(
    private val repository: StatsRepository
): UseCaseWithoutParams<Unit> {
//    private val workManager: WorkManager = WorkManager.getInstance(context)
    override suspend fun execute() {
//        StatsUpdateWorker.enqueue(workManager)
        repository.updateStatsInBackground()
    }
}