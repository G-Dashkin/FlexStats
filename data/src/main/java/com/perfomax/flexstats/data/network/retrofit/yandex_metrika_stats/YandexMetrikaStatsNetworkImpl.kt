package com.perfomax.flexstats.data.network.retrofit.yandex_metrika_stats

import android.util.Log
import com.perfomax.flexstats.core.utils.CLIENT_ID
import com.perfomax.flexstats.core.utils.CLIENT_SECRET
import com.perfomax.flexstats.core.utils.DIRECT_API_BASE_URL
import com.perfomax.flexstats.core.utils.METRIKA_API_BASE_URL
import com.perfomax.flexstats.data.database.dao.StatsDao
import com.perfomax.flexstats.data_api.network.YandexDirectStatsNetwork
import com.perfomax.flexstats.data_api.network.YandexMetrikaStatsNetwork
import com.perfomax.flexstats.data_api.storage.AuthStorage
import com.perfomax.flexstats.models.YandexDirectStats
import com.perfomax.flexstats.models.YandexMetrikaStats
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class YandexMetrikaStatsNetworkImpl @Inject constructor(
    private val statsDao: StatsDao,
    private val authStorage: AuthStorage
): YandexMetrikaStatsNetwork {

    override suspend fun getStats(date: String, metrikaCounter: String, token: String): List<YandexMetrikaStats> {

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
            metrics = "ym:s:ecommercePurchases",
            dimensions = "ym:s:lastsignUTMMedium",
        )
        val x = yandexMetrikaStatsCall.execute()
        Log.d("MyLog", x.body()?.data.toString())
        return listOf()

//        var result = yandexMetrikaStatsCall.execute()
//        var count = 1
//        while (result.code() != 200) {
//            result = yandexMetrikaStatsCall.clone().execute()
//            count = count.inc()
//            delay(100)
//        }
//        val request = yandexMetrikaStatsCall.clone().request()
//        val newClient = OkHttpClient()
//        val dataYD = newClient.newCall(request).execute()
//
//        val yandexStringsData = mutableListOf<List<String>>()
//        dataYD.body.also {
//            it?.byteStream()?.bufferedReader()?.forEachLine { stats ->
//                yandexStringsData.add(stats.split("\\s".toRegex()))
//            }
//        }
//        yandexStringsData.removeAt(0)
//
//        val yandexData = mutableListOf<YandexDirectStats>()
//
//        yandexStringsData.forEach {
//            yandexData.add(YandexDirectStats(
//                date = date,
//                impressions = it[0].toInt(),
//                clicks = it[1].toInt(),
//                cost = it[2].toDouble()
//            ))
//        }

//        return yandexData
//        Log.d("MyLog", yandexData.toString())
//        yandexData.forEach { dataList ->
//            Log.d("MyLog", dataList.toString())
//            statsDao.insertYandexDirect(
//                YandexDirectStatsEntity(
//                    id = 0,
//                    date = date,
//                    account = account,
//                    campaign = dataList[0],
//                    impressions = dataList[1].toInt(),
//                    clicks = dataList[2].toInt(),
//                    cost = dataList[3].toDouble(),
//                    project_id = authStorage.getAuthUser().id?:0
//                )
//            )
//        }
    }
}