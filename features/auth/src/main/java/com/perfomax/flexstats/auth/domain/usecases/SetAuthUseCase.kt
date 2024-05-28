package com.perfomax.flexstats.auth.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithParams
import com.perfomax.flexstats.core.contracts.UseCaseWithoutParams
import com.perfomax.flexstats.models.User
import com.perfomax.flexstats.data_api.repository.AuthRepository
import javax.inject.Inject

class SetAuthUseCase @Inject constructor(
    private val repository: AuthRepository
): UseCaseWithoutParams<Unit> {
    override suspend fun execute() {
        repository.setAuth()
    }
}