package com.perfomax.flexstats.models

data class YandexMetrikaStats (
    var id: Int? = null,
    var date: String? = null,
    var accoutn: String? = null,
    var counter: String? = null,
    var transactions: Int? = null,
    var revenue: Long? = null,
    var project_id: Int? = null
)