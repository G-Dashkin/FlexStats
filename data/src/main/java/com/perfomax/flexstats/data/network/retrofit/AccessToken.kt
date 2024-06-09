package com.perfomax.flexstats.data.network.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AccessToken (
    @SerializedName("access_token")
    @Expose
    val access_token: String
)