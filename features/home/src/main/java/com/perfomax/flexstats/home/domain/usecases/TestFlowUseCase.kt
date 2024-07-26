package com.perfomax.flexstats.home.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithParams
import com.perfomax.flexstats.core.contracts.UseCaseWithoutParams
import com.perfomax.flexstats.data_api.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TestFlowUseCase @Inject constructor(
    private val repository: StatsRepository
): UseCaseWithoutParams<Flow<String>> {
    override suspend fun execute(): Flow<String> {
        return repository.testFlow()
    }
}