package com.perfomax.flexstats.data_api.storage

import com.perfomax.flexstats.models.Account

interface AccountsStorage {
    suspend fun add(account: Account)
    suspend fun delete(accountId: Int)
    suspend fun getAllAccountsOfUser(projectId: Int): List<Account>
}