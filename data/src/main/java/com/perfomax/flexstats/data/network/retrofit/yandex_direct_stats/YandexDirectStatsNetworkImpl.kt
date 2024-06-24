package com.perfomax.flexstats.data.network.retrofit.yandex_direct_stats

import com.perfomax.flexstats.core.utils.DIRECT_API_BASE_URL
import com.perfomax.flexstats.data.database.dao.StatsDao
import com.perfomax.flexstats.data_api.network.YandexDirectStatsNetwork
import com.perfomax.flexstats.data_api.storage.AuthStorage
import com.perfomax.flexstats.models.YandexDirectStats
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class YandexDirectStatsNetworkImpl @Inject constructor(
    private val statsDao: StatsDao,
    private val authStorage: AuthStorage
): YandexDirectStatsNetwork {

    override suspend fun getStats(date: String, account: String, token: String): List<YandexDirectStats> {

        val bodyFields = mapOf(
            "params" to mapOf(
                "SelectionCriteria" to mapOf(
                    "DateFrom" to date,
                    "DateTo" to date
                ),
                "FieldNames" to arrayListOf(
                    "Impressions",
                    "Clicks",
                    "Cost"
                ),
                "ReportName" to "$account ${System.currentTimeMillis()}",
                "ReportType" to "CUSTOM_REPORT",
                "DateRangeType" to "CUSTOM_DATE",
                "Format" to "TSV",
                "IncludeVAT" to "YES",
                "IncludeDiscount" to "NO"
            )
        )

        val retrofit = Retrofit.Builder()
            .baseUrl(DIRECT_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val yandexDirectStatsApi = retrofit.create(YandexDirectStatsApi::class.java)

        val yandexDirectStatsCall = yandexDirectStatsApi.getData(
            body_fields = bodyFields,
            login = account,
            token = "Bearer $token"
        )

        var result = yandexDirectStatsCall.execute()
        var count = 1
        while (result.code() != 200) {
            result = yandexDirectStatsCall.clone().execute()
            count = count.inc()
            delay(100)
        }

        val request = yandexDirectStatsCall.clone().request()
        val newClient = OkHttpClient()
        val dataYD = newClient.newCall(request).execute()

        val yandexStringsData = mutableListOf<List<String>>()
        dataYD.body.also {
            it?.byteStream()?.bufferedReader()?.forEachLine { stats ->
                yandexStringsData.add(stats.split("\\s".toRegex()))
            }
        }
        yandexStringsData.removeAt(0)

        val yandexData = mutableListOf<YandexDirectStats>()

        yandexStringsData.forEach {
            yandexData.add(YandexDirectStats(
                date = date,
                account = account,
                impressions = it[0].toInt(),
                clicks = it[1].toInt(),
                cost = it[2].toDouble()
            ))
        }

        return yandexData
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