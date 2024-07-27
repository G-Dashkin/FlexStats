package com.perfomax.flexstats.home.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithParams
import com.perfomax.flexstats.data_api.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateStatsUseCase @Inject constructor(
    private val repository: StatsRepository
): UseCaseWithParams<Flow<String>, Pair<String, String>> {
    override suspend fun execute(updatePeriod: Pair<String, String>): Flow<String> {
        return repository.updateStats(updatePeriod)
    }
}