package com.perfomax.flexstats.data.network.retrofit.yandex_metrika_stats

import com.perfomax.flexstats.core.contracts.METRIKA_API_BASE_URL
import com.perfomax.flexstats.data.database.dao.StatsDao
import com.perfomax.flexstats.data_api.network.YandexMetrikaStatsNetwork
import com.perfomax.flexstats.data_api.storage.AuthStorage
import com.perfomax.flexstats.models.YandexMetrikaStats
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class YandexMetrikaStatsNetworkImpl @Inject constructor(
    private val statsDao: StatsDao,
    private val authStorage: AuthStorage
): YandexMetrikaStatsNetwork {

    override suspend fun getStats(
        date: String,
        account: String,
        metrikaCounter: String,
        token: String,
        projectId: Int
    ): YandexMetrikaStats {

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
        val yandexMetrikaStatsCall = yandexMetrikaStatsApi.getData(
            token = "OAuth $token",
            id = metrikaCounter,
            date1 = date,
            date2 = date,
            filters = "ym:s:lastsignUTMMedium=='cpc' AND ym:s:lastsignUTMSource=='yandex'",
            metrics = "ym:s:ecommercePurchases, ym:s:ecommerceRUBConvertedRevenue",
            dimensions = "ym:s:lastsignUTMMedium, ym:s:lastsignUTMSource",
        )
        val yandexMetrikaStats = yandexMetrikaStatsCall.execute()

        return YandexMetrikaStats(
            date = date,
            accoutn = account,
            counter = metrikaCounter,
            transactions = yandexMetrikaStats.body()?.totals?.get(0)?.toInt(),
            revenue = yandexMetrikaStats.body()?.totals?.get(1)?.toLong(),
            project_id = projectId
        )
    }

}