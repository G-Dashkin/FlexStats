package com.perfomax.flexstats.data.repository

import com.perfomax.flexstats.core.utils.isNotMaxUpdateDate
import java.time.format.DateTimeFormatter

val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun main() {

    println("2024-02-01".isNotMaxUpdateDate(6))
}

