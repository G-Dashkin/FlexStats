package com.perfomax.flexstats.data.network.retrofit.yandex_metrika_stats

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface YandexMetrikaStatsApi {
    @GET("stat/v1/data")
    fun getData(
        @Header("Authorization") token: String,
        @Query("id") id: String,
        @Query("date1") date1: String,
        @Query("date2") date2: String,
        @Query("metrics") metrics: String,
        @Query("dimensions") dimensions: String,
        @Query("filters") filters: String = "",
        @Query("accuracy") accuracy: String = "1",
        @Query("limit") limit: String = "10000"
    ): Call<YandexMetrikaStatsPojo>
}