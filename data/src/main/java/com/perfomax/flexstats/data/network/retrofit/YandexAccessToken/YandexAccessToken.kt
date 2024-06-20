package com.perfomax.flexstats.data.network.retrofit.YandexAccessToken

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class YandexAccessToken (
    @SerializedName("access_token")
    @Expose
    val access_token: String
)