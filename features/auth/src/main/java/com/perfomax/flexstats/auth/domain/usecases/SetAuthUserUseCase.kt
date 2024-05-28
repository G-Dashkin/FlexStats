package com.perfomax.flexstats.auth.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithParams
import com.perfomax.flexstats.data_api.repository.AuthRepository
import com.perfomax.flexstats.models.User
import javax.inject.Inject

class SetAuthUserUseCase @Inject constructor(
    private val repository: AuthRepository
): UseCaseWithParams<Unit, User> {
    override suspend fun execute(authUser: User) {
        repository.setAuthUser(authUser = authUser)
    }
}