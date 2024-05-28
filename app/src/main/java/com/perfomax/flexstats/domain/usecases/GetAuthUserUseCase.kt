package com.perfomax.flexstats.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithoutParams
import com.perfomax.flexstats.data_api.repository.AuthRepository
import com.perfomax.flexstats.models.User
import javax.inject.Inject

class GetAuthUserUseCase @Inject constructor(
    private val repository: AuthRepository
): UseCaseWithoutParams<User> {
    override suspend fun execute():User {
        return repository.getAuthUser()
    }
}