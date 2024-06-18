package com.perfomax.flexstats.start.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithoutParams
import com.perfomax.flexstats.data_api.repository.AuthRepository
import javax.inject.Inject

class GetAuthUseCase @Inject constructor(
    private val repository: AuthRepository
): UseCaseWithoutParams<Boolean> {
    override suspend fun execute(): Boolean {
        return repository.getAuth()
    }
}