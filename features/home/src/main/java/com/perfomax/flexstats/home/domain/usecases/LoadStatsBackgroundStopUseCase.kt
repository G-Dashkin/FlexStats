package com.perfomax.flexstats.home.domain.usecases

//import androidx.work.WorkManager
import com.perfomax.flexstats.core.contracts.UseCaseWithoutParams
import com.perfomax.flexstats.data_api.repository.StatsRepository
import javax.inject.Inject

class LoadStatsBackgroundStopUseCase @Inject constructor(
    private val repository: StatsRepository
): UseCaseWithoutParams<Unit> {
    override suspend fun execute() {
        repository.updateStatsInBackgroundStop()
    }
}