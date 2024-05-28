package com.perfomax.flexstats.auth.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithParams
import com.perfomax.flexstats.models.User
import com.perfomax.flexstats.data_api.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
): UseCaseWithParams<Unit, User> {
    override suspend fun execute(newUser: User) {
        repository.create(newUser = newUser)
    }
}