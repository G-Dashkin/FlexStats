package com.perfomax.flexstats.data_api.storage

import com.perfomax.flexstats.models.Account
import com.perfomax.flexstats.models.Project

interface AccountsStorage {
    suspend fun add(project: Account)
    suspend fun getAllAccountsOfUser(projectId: Int): List<Account>
}