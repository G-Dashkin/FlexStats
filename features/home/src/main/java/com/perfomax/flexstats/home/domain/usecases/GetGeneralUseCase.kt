package com.perfomax.flexstats.home.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithoutParams
import com.perfomax.flexstats.data_api.repository.StatsRepository
import com.perfomax.flexstats.models.GeneralStats
import com.perfomax.flexstats.models.YandexDirectStats
import javax.inject.Inject

class GetGeneralUseCase @Inject constructor(
    private val repository: StatsRepository
): UseCaseWithoutParams<List<GeneralStats>> {
    override suspend fun execute(): List<GeneralStats> {
        return repository.getGeneralStats()
    }
}