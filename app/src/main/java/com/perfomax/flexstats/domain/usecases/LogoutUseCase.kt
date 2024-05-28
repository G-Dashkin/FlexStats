package com.perfomax.flexstats.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithoutParams
import com.perfomax.flexstats.data_api.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
): UseCaseWithoutParams<Unit> {
    override suspend fun execute() {
        return repository.logout()
    }
}