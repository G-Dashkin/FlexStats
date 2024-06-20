package com.perfomax.flexstats.data.network.retrofit.YandexDirectStats

import android.util.Log
import com.perfomax.flexstats.core.utils.DIRECT_API_TOKEN_URL
import com.perfomax.flexstats.data.database.dao.YandexDirectStatsDao
import com.perfomax.flexstats.data.database.entities.YandexDirectStatsEntity
import com.perfomax.flexstats.data_api.network.YandexDirectStatsNetwork
import com.perfomax.flexstats.models.Stats
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class YandexDirectStatsNetworkImpl @Inject constructor(
    private val yandexDirectStatsDao: YandexDirectStatsDao
): YandexDirectStatsNetwork {

//    private val flexStatDao = db.flexStatDao()
    override suspend fun getStats(date: String, account: String, token: String) {

        val bodyFields = mapOf(
            "params" to mapOf(
                "SelectionCriteria" to mapOf(
                    "DateFrom" to date,
                    "DateTo" to date
                ),
                "FieldNames" to arrayListOf(
                    "CampaignName",
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
            .baseUrl(DIRECT_API_TOKEN_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val yandexDirectStatsApi = retrofit.create(YandexDirectStatsApi::class.java)


        val yandexDirectStatsCall = yandexDirectStatsApi.getData(
            body_fields = bodyFields,
            login = account,
            token = "Bearer $token"
        )
        GlobalScope.launch {
            var result = yandexDirectStatsCall.execute()
            var count = 1
            while (result.code() != 200) {
                result = yandexDirectStatsCall.clone().execute()
                count = count.inc()
                Log.d("MyLog", result.errorBody()!!.charStream().readText())
                delay(1000)
                Log.d("MyLog", "----------------------------------------------------------")
            }

            val request = yandexDirectStatsCall.clone().request()
            val newClient = OkHttpClient()
            val dataYD = newClient.newCall(request).execute()

            val yandexData = mutableListOf<List<String>>()
            dataYD.body.also {
                it?.byteStream()?.bufferedReader()?.forEachLine { stats ->
                    yandexData.add(stats.split("\\s".toRegex()))
                }
            }
            Log.d("MyLog", yandexData[0].toString())
            yandexData.removeAt(0)
            yandexData.forEach {
                Log.d("MyLog", it.toString())
            }

            yandexDirectStatsDao.insert(YandexDirectStatsEntity(
                id = 0,
                date = "dfdfd",
                account = "dddd",
                impressions = 1000,
                clicks = 100,
                cost = "14".toFloat()
            ))
        }
    }
}