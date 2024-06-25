package com.perfomax.flexstats.data.network.retrofit.yandex_access_token

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class YandexAccessTokenPojo (
    @SerializedName("access_token")
    @Expose
    val access_token: String
)