package com.perfomax.flexstats.accounts.domain

import com.perfomax.flexstats.core.contracts.UseCaseWithParams
import com.perfomax.flexstats.data_api.repository.AccountsRepository
import com.perfomax.flexstats.models.Account
import javax.inject.Inject

class CreateTokenUseCase @Inject constructor(
    private val repository: AccountsRepository
): UseCaseWithParams<String, String> {
    override suspend fun execute(tokenCode: String): String {
        return repository.createToken(tokenCode = tokenCode)
    }
}