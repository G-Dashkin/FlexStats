package com.perfomax.flexstats.auth.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithoutParams
import com.perfomax.flextats.data_api.repository.AuthRepository
import javax.inject.Inject

class GetAuthUseCase @Inject constructor(
    private val repository: AuthRepository
): UseCaseWithoutParams<String> {
    override suspend fun execute(): String {
        return repository.getAuth()
    }
}