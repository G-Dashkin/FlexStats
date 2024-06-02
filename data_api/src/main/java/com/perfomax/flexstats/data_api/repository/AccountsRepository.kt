package com.perfomax.flexstats.data_api.repository

import com.perfomax.flexstats.models.Account
import com.perfomax.flexstats.models.Project

interface AccountsRepository {
    suspend fun create(account: Account)
    suspend fun delete(accountId: Int)
    suspend fun getUserAll(): List<Account>
}