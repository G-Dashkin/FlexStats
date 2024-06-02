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
    override suspend fun add(project: Account) {
        accountsDao.insert(project.toDomain())
    }

    override suspend fun getAllAccountsOfUser(projectId: Int): List<Account> {
        return accountsDao.getAllAccountOfUser(projectId.toString()).map { it.toDomain() }
    }
}