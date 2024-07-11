package com.perfomax.flexstats.data.repository

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter

const val DEFAULT_UPDATE_DAYS = 3

fun main() {
//    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//    for(i in 1..DEFAULT_UPDATE_DAYS) {
//        val update_date = LocalDateTime.now().minusDays(i.toLong()).format(formatter)
//        println("update_date: $update_date")
//    }

    val startDate = "2024-07-02"
    val endDate = "2024-07-08"

    val date1 = LocalDate.of(
        startDate.split("-")[0].toInt(),
        startDate.split("-")[1].toInt(),
        startDate.split("-")[2].toInt()
    )
    val date2 = LocalDate.of(
        endDate.split("-")[0].toInt(),
        endDate.split("-")[1].toInt(),
        endDate.split("-")[2].toInt()
    )
    val period = Period.between(date1, date2)

    println("Days ${period.days}")
}


