package com.perfomax.flexstats.data_api.network

import com.perfomax.flexstats.models.Account

interface YandexAccessTokenNetwork {
    suspend fun getToken(tokenCode: String): String
    suspend fun chekMetrikaCounterExists(account: Account):Boolean
}