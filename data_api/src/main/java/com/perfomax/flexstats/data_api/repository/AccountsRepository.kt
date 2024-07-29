package com.perfomax.flexstats.data_api.repository

import com.perfomax.flexstats.models.Account

interface AccountsRepository {
    suspend fun createAccount(account: Account)
    suspend fun createYandexToken(tokenCode: String): String
    suspend fun deleteAccount(accountId: Int)
    suspend fun getAllAccountsByUser(): List<Account>
    suspend fun checkMetrikaCounter(account: Account): Boolean
}