package com.perfomax.flexstats.start.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithoutParams
import com.perfomax.flexstats.models.User
import com.perfomax.flextats.data_api.repository.AuthRepository
import javax.inject.Inject

class GetAuthUseCase @Inject constructor(
    private val repository: AuthRepository
): UseCaseWithoutParams<User> {
    override suspend fun execute(): User {
        return repository.getAuth()
    }
}