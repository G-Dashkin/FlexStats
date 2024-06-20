package com.perfomax.flexstats.models

import java.time.LocalDate

data class Orders (
    var date: LocalDate? = null,
    var source: String? = null,
    var campaign: String? = null,
    var device: String? = null,
    var transactions: Int? = null,
    var revenue: Float? = null
)