package com.perfomax.flexstats.data.network.retrofit

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ProductApi {
    @JvmSuppressWildcards
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("token")
    suspend fun getAccessToken(@Body data: String): AccessToken
}