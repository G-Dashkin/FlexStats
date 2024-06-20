package com.perfomax.flexstats.models

import java.time.LocalDate

data class Stats (
    var date: LocalDate? = null,
    var account: String? = null,
    var source: String? = null,
    var campaign: String? = null,
    var device: String? = null,
    var impressions: Int? = null,
    var clicks: Int? = null,
    var cost: Float? = null
)