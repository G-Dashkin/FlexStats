package com.perfomax.flexstats.home.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithoutParams
import com.perfomax.flexstats.data_api.repository.StatsRepository
import com.perfomax.flexstats.models.YandexDirectStats
import javax.inject.Inject

class GetStatsUseCase @Inject constructor(
    private val repository: StatsRepository
): UseCaseWithoutParams<List<YandexDirectStats>> {
    override suspend fun execute(): List<YandexDirectStats> {
        return repository.getStats()
    }
}