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
//    @GET("stat/v1/data")
//    fun getData(
//        @Body body_fields: Map<String, Any>?,
//        @Header("Authorization") token: String,
//    ): Call<Unit>

    @JvmSuppressWildcards
    @GET("/stat/v1/data?")
    fun getData(
        @Header("Authorization") token: String,
        @Url body_fields: String
    ): Call<Unit>

//    @GET("users/{userId}")
//    fun getUserById(@Path("userId") userId: Int): Call<User>

}