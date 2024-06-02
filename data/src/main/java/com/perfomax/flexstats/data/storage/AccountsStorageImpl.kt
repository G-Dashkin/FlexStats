package com.perfomax.flexstats.data.storage

import com.perfomax.flexstats.data.database.dao.AccountsDao
import com.perfomax.flexstats.data.database.dao.ProjectsDao
import com.perfomax.flexstats.data.mappers.toDomain
import com.perfomax.flexstats.data_api.storage.AccountsStorage
import com.perfomax.flexstats.models.Account
import javax.inject.Inject

class AccountsStorageImpl @Inject constructor(
    private val accountsDao: AccountsDao
): AccountsStorage {
    override suspend fun add(account: Account) {
        accountsDao.insert(newAccount = account.toDomain())
    }

    override suspend fun delete(accountId: Int) {
        accountsDao.delete(accountId = accountId.toString())
    }

    override suspend fun getAllAccountsOfUser(projectId: Int): List<Account> {
        return accountsDao.getAllAccountOfUser(projectId = projectId.toString()).map { it.toDomain() }
    }
}