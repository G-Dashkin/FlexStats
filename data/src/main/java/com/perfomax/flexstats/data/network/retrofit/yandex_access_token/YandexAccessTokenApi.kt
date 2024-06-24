package com.perfomax.flexstats.data.network.retrofit.yandex_access_token

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface YandexAccessTokenApi {
    @JvmSuppressWildcards
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("token")
    suspend fun getAccessToken(@Body data: String): YandexAccessToken
}