package com.perfomax.flexstats.auth.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithParams
import com.perfomax.flextats.data_api.repository.AuthRepository
import javax.inject.Inject

class SetAuthUseCase @Inject constructor(
    private val repository: AuthRepository
): UseCaseWithParams<Unit, String> {
    override suspend fun execute(userName: String) {
        repository.setAuth(userName = userName)
    }
}