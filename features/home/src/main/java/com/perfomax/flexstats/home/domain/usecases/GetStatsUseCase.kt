package com.perfomax.flexstats.home.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithoutParams
import com.perfomax.flexstats.data_api.repository.AuthRepository
import com.perfomax.flexstats.data_api.repository.StatsRepository
import com.perfomax.flexstats.models.Stats
import com.perfomax.flexstats.models.User
import javax.inject.Inject

class GetStatsUseCase @Inject constructor(
    private val repository: StatsRepository
): UseCaseWithoutParams<Unit> {
    override suspend fun execute(): Unit {
        repository.getStats()
        return Unit
    }
}