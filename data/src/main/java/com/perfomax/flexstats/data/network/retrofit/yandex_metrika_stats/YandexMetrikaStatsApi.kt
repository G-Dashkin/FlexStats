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

    //    @JvmSuppressWildcards
    @GET("stat/v1/data?id=155462&date1=2024-02-29&date2=2024-02-29&metrics=ym:s:ecommercePurchases&dimensions=ym:s:lastsignUTMMedium&accuracy=full&limit=10000")
    fun getData(
        @Header("Authorization") token: String
    ): Call<Unit>

}