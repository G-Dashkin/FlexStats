package com.perfomax.flexstats.data.network

import android.util.Log
import com.perfomax.flexstats.data.network.retrofit.ProductApi
import com.perfomax.flexstats.data_api.network.TokenNetwork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class TokenNetworkImpl: TokenNetwork {

    override suspend fun getToken(tokenCode: String): String  {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://oauth.yandex.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val productApi = retrofit.create(ProductApi::class.java)

        val data = "&grant_type=authorization_code" +
                   "&code=$tokenCode" +
                   "&client_id=4f078917869842f2932a9d30fa5d0bb5" +
                   "&client_secret=b7c67382bff24f2b89ccecc5e7ade0a4&"

        val accessToken = productApi.getAccessToken(data)

        return accessToken.access_token
    }
}