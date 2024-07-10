package com.perfomax.flexstats.models

data class GeneralStats (
    var id: Int? = null,
    var date: String? = null,
    var impressions: Int? = null,
    var clicks: Int? = null,
    var cost: Long? = null,
    var transactions: Int? = null,
    var revenue: Long? = null,
    var project_id: Int? = null
)