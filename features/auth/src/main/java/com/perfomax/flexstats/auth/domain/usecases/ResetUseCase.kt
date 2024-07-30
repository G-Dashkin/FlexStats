package com.perfomax.flexstats.auth.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithParams
import com.perfomax.flexstats.data_api.repository.AuthRepository
import javax.inject.Inject

class ResetUseCase @Inject constructor(
    private val repository: AuthRepository
): UseCaseWithParams<Boolean, String> {
    override suspend fun execute(email: String):Boolean {
        return repository.reset(email = email)
    }
}