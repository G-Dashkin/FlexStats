package com.perfomax.flexstats.accounts.domain

import com.perfomax.flexstats.core.contracts.UseCaseWithoutParams
import com.perfomax.flexstats.data_api.repository.AccountsRepository
import com.perfomax.flexstats.models.Account
import javax.inject.Inject

class GetAccountsByProjectUseCase @Inject constructor(
    private val repository: AccountsRepository
): UseCaseWithoutParams<List<Account>> {
    override suspend fun execute(): List<Account> {
        return repository.getAllByUser()
    }
}