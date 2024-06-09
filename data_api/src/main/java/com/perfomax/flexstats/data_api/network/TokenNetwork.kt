package com.perfomax.flexstats.data_api.network

interface TokenNetwork {
    suspend fun getToken(tokenCode: String): String
}