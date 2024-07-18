package com.perfomax.flexstats.data.network.retrofit.yandex_access_token

import com.perfomax.flexstats.core.contracts.BASE_OAUTH_TOKEN_URL
import com.perfomax.flexstats.core.contracts.CLIENT_ID
import com.perfomax.flexstats.core.contracts.CLIENT_SECRET
import com.perfomax.flexstats.data_api.network.YandexAccessTokenNetwork
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class YandexAccessTokenNetworkImpl: YandexAccessTokenNetwork {

    override suspend fun getToken(tokenCode: String): String  {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_OAUTH_TOKEN_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val yandexAccessTokenApi = retrofit.create(YandexAccessTokenApi::class.java)

        val data = "&grant_type=authorization_code&code=$tokenCode" +
                   "&client_id=$CLIENT_ID" +
                   "&client_secret=$CLIENT_SECRET&"

        val accessToken = yandexAccessTokenApi.getAccessToken(data)

        return accessToken.access_token
    }
}