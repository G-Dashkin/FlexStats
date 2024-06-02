package com.perfomax.flexstats.accounts.domain

import com.perfomax.flexstats.core.contracts.UseCaseWithParams
import com.perfomax.flexstats.data_api.repository.AccountsRepository
import com.perfomax.flexstats.models.Account
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val repository: AccountsRepository
): UseCaseWithParams<Unit, Int> {
    override suspend fun execute(accountId: Int) {
        repository.delete(accountId = accountId)
    }
}