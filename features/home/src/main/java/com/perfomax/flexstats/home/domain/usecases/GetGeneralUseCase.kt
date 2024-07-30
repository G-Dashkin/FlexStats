package com.perfomax.flexstats.home.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithParams
import com.perfomax.flexstats.data_api.repository.StatsRepository
import com.perfomax.flexstats.models.GeneralStats
import javax.inject.Inject

class GetGeneralUseCase @Inject constructor(
    private val repository: StatsRepository
): UseCaseWithParams<List<GeneralStats>, Pair<String, String>> {
    override suspend fun execute(statsPeriod: Pair<String, String>): List<GeneralStats> {
        return repository.getGeneralStats(statsPeriod)
    }
}