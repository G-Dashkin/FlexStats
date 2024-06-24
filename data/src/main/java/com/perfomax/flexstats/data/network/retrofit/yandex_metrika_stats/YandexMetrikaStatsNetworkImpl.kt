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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class YandexMetrikaStatsNetworkImpl @Inject constructor(
    private val statsDao: StatsDao,
    private val authStorage: AuthStorage
): YandexMetrikaStatsNetwork {

    override suspend fun getStats(date: String, metrikaCounter: String, token: String){

//        val bodyFields = mapOf(
//            "id" to metrikaCounter,
//            "date1" to date,
//            "date2" to date,
//            "metrics" to "ym:s:ecommercePurchases, ym:s:ecommerceRUBConvertedRevenue",
//            "dimensions" to "ym:s:lastsignUTMMedium, ym:s:lastsignUTMSource",
//            "accuracy" to "full",
//            "limit" to 10000
//        )

        val retrofit = Retrofit.Builder()
            .baseUrl(METRIKA_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val yandexMetrikaStatsApi = retrofit.create(YandexMetrikaStatsApi::class.java)


        val bodyFields = "$metrikaCounter" +
                            "&date1=$date" +
                            "&date2=$date" +
                            "&metrics=ym:s:ecommercePurchases" +
                            "&dimensions=ym:s:lastsignUTMMedium" +
                            "&accuracy=full" +
                            "&limit=10000"

        val yandexMetrikaStatsCall = yandexMetrikaStatsApi.getData(
            token = "OAuth $token",
            body_fields = bodyFields
        )

        val metrikaStatsRequest = yandexMetrikaStatsCall.request()
        Log.d("MyLog", metrikaStatsRequest.toString())
//        Log.d("MyLog", yandexMetrikaStatsCall.errorBody()!!.string())
        var result = yandexMetrikaStatsCall.execute()
//
        Log.d("MyLog", result.toString())

//        var result = yandexMetrikaStatsCall.execute()
//        var count = 1
//        while (result.code() != 200) {
//            result = yandexMetrikaStatsCall.clone().execute()
//            count = count.inc()
//            delay(100)
//        }
//
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