package com.perfomax.flexstats.auth.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithoutParams
import com.perfomax.flexstats.models.User
import com.perfomax.flextats.data_api.repository.AuthRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: AuthRepository
): UseCaseWithoutParams<List<User>> {
    override suspend fun execute(): List<User> {
        return repository.getAll()
    }
}