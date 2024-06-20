package com.perfomax.flexstats.data.network.retrofit.YandexDirectStats

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface YandexDirectStatsApi {
    @JvmSuppressWildcards
    @Headers(
        "Accept-Language:ru",
        "skipReportHeader:true",
        "skipReportSummary:true",
        "returnMoneyInMicros:false",
        "processingMode:auto"
    )
    @POST("json/v5/reports")
    fun getData(
        @Body body_fields: Map<String, Map<String, Any>>?,
        @Header("Client-Login") login: String,
        @Header("Authorization") token: String,
    ): Call<Unit>
}