package com.perfomax.flexstats.models

data class YandexDirectStats (
    var id: Int? = null,
    var date: String? = null,
    var account: String? = null,
    var impressions: Int? = null,
    var clicks: Int? = null,
    var cost: Long? = null,
    var project_id: Int? = null
)