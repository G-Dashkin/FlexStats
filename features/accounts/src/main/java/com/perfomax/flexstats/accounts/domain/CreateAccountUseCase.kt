package com.perfomax.flexstats.accounts.domain

import com.perfomax.flexstats.core.contracts.UseCaseWithParams
import com.perfomax.flexstats.data_api.repository.AccountsRepository
import com.perfomax.flexstats.data_api.repository.ProjectsRepository
import com.perfomax.flexstats.models.Account
import com.perfomax.flexstats.models.Project
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val repository: AccountsRepository
): UseCaseWithParams<Unit, Account> {
    override suspend fun execute(newAccount: Account) {
        repository.create(account = newAccount)
    }
}