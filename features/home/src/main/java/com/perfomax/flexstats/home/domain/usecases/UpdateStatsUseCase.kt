package com.perfomax.flexstats.home.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithParams
import com.perfomax.flexstats.core.contracts.UseCaseWithoutParams
import com.perfomax.flexstats.data_api.repository.StatsRepository
import javax.inject.Inject

class UpdateStatsUseCase @Inject constructor(
    private val repository: StatsRepository
): UseCaseWithParams<Unit, Pair<String, String>> {
    override suspend fun execute(updatePeriod: Pair<String, String>) {
        repository.updateStats(updatePeriod)
    }
}