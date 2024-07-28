package com.perfomax.flexstats.data_api.repository

import com.perfomax.flexstats.models.Account

interface AccountsRepository {
    suspend fun create(account: Account)
    suspend fun createToken(tokenCode: String): String
    suspend fun delete(accountId: Int)
    suspend fun getAllAccountsByUser(): List<Account>
}