package com.perfomax.flexstats.auth.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithoutParams
import com.perfomax.flexstats.data_api.repository.AuthRepository
import com.perfomax.flexstats.models.User
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val repository: AuthRepository
): UseCaseWithoutParams<List<User>> {
    override suspend fun execute(): List<User> {
        return repository.getAll()
    }
}