package com.perfomax.flexstats.accounts.domain

import com.perfomax.flexstats.core.contracts.UseCaseWithParams
import com.perfomax.flexstats.data_api.repository.AccountsRepository
import javax.inject.Inject

class CreateTokenUseCase @Inject constructor(
    private val repository: AccountsRepository
): UseCaseWithParams<String, String> {
    override suspend fun execute(tokenCode: String): String {
        return repository.createYandexToken(tokenCode = tokenCode)
    }
}