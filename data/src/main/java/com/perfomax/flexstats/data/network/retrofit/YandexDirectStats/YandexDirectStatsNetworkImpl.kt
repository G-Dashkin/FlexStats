package com.perfomax.flexstats.data.network.retrofit.YandexDirectStats

import android.util.Log
import com.perfomax.flexstats.core.utils.DIRECT_API_TOKEN_URL
import com.perfomax.flexstats.data.network.retrofit.YandexAccessToken.YandexAccessTokenApi
import com.perfomax.flexstats.data_api.network.YandexDirectStatsNetwork
import com.perfomax.flexstats.models.Stats
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class YandexDirectStatsNetworkImpl: YandexDirectStatsNetwork {
    override suspend fun getStats(date: String, account: String, token: String){

        val body_fields = mapOf(
            "params" to mapOf(
                "SelectionCriteria" to mapOf(
                    "DateFrom" to date,
                    "DateTo" to date
                ),
                "FieldNames" to arrayListOf("CampaignName", "Impressions", "Impressions", "Clicks", "Cost"),
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
        val yandexDirectStatsNetwork = retrofit.create(YandexDirectStatsApi::class.java)
        val yandexDirectStatsCall = yandexDirectStatsNetwork.getData(body_fields = body_fields, login = "Bearer " + account, token = token)
        var result = yandexDirectStatsCall.execute()

//        val tokenCall = reportsService.getData(body_fields, account, "Bearer " + accounts[account])
//        var result = tokenCall.execute()

        var count = 1
        while (result.code() != 200) {
            result = yandexDirectStatsCall.clone().execute()
            count = count.inc()
        }

        val request = yandexDirectStatsCall.clone().request()
        val newClient = OkHttpClient()
        val dataYD = newClient.newCall(request).execute()

        var yandexData = listOf<String>()
//        Log.d("MyLog", "yandexData.toString()")
        dataYD.body.also {
            it?.byteStream()?.bufferedReader()?.forEachLine {
                yandexData = it.split("\\s".toRegex())

                Log.d("MyLog", yandexData.toString())
            }
        }


    }
}