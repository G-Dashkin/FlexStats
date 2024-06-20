package com.perfomax.flexstats.data_api.network

interface YandexAccessTokenNetwork {
    suspend fun getToken(tokenCode: String): String
}