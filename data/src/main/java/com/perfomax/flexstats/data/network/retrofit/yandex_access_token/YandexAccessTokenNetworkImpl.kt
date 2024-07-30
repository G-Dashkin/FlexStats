package com.perfomax.flexstats.data.network.retrofit.yandex_access_token

import android.util.Log
import com.perfomax.flexstats.core.contracts.BASE_OAUTH_TOKEN_URL
import com.perfomax.flexstats.core.contracts.CLIENT_ID
import com.perfomax.flexstats.core.contracts.CLIENT_SECRET
import com.perfomax.flexstats.core.contracts.DATE_FORMAT
import com.perfomax.flexstats.core.contracts.EMPTY
import com.perfomax.flexstats.core.contracts.METRIKA_API_BASE_URL
import com.perfomax.flexstats.core.utils.isNotMaxUpdateDate
import com.perfomax.flexstats.core.utils.toTimestamp
import com.perfomax.flexstats.data.network.retrofit.yandex_metrika_stats.YandexMetrikaStatsApi
import com.perfomax.flexstats.data_api.network.YandexAccessTokenNetwork
import com.perfomax.flexstats.models.Account
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

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

    override suspend fun chekMetrikaCounterExists(account: Account): Boolean {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(METRIKA_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val yandexMetrikaStatsApi = retrofit.create(YandexMetrikaStatsApi::class.java)

        val yandexMetrikaCheckCall = yandexMetrikaStatsApi.getData(
            token = "OAuth ${account.accountToken}",
            id = account.metrikaCounter?: EMPTY,
            date1 = LocalDate.now().minusDays(1).toString(),
            date2 = LocalDate.now().minusDays(1).toString(),
            metrics = "ym:s:visits",
            dimensions = "ym:s:lastsignUTMMedium",
        ).execute()

        return yandexMetrikaCheckCall.code() != 404
    }
}