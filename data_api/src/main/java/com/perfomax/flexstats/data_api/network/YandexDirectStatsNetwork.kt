package com.perfomax.flexstats.data_api.network


interface YandexDirectStatsNetwork {
    suspend fun getStats(date: String, account: String, token: String)
}