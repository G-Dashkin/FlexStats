package com.perfomax.flexstats.data.repository

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun main() {

    // 1) Типа дата первого обновления аккаунта полученная из базы и дата за вчерашний день
    val firstUpdateDate = "2024-06-01"
    val yesterday = LocalDateTime.now().minusDays(1).format(formatter)

    // 2) Получение даты первого обновления аккаунта минус 1 месяц
    val startUpdateDate = LocalDate.of(
        firstUpdateDate.split("-")[0].toInt(),
        firstUpdateDate.split("-")[1].toInt(),
        firstUpdateDate.split("-")[2].toInt(),
    ).minusMonths(1)

    val endUpdateDate = LocalDate.of(
        firstUpdateDate.split("-")[0].toInt(),
        firstUpdateDate.split("-")[1].toInt(),
        firstUpdateDate.split("-")[2].toInt(),
    )

    // 3) Вычисление количества дней между вчерашнем днем и датой перого обновления базы (минус 1 месяц)
    val secondTimestampExclusive = LocalDate.of(
        yesterday.split("-")[0].toInt(),
        yesterday.split("-")[1].toInt(),
        yesterday.split("-")[2].toInt(),
    )

    val firstUpdateDays = Duration.between(
        startUpdateDate.atStartOfDay(),
        secondTimestampExclusive.atStartOfDay())
        .toDays()
        .toInt()
        .inc()

    val lastUpdateDays = Duration.between(
        endUpdateDate.atStartOfDay(),
        secondTimestampExclusive.atStartOfDay())
        .toDays()
        .toInt().inc()
        .inc()

//    for (day in lastUpdateDays ..firstUpdateDays){
//        val updateDate = LocalDateTime.now().minusDays(day.toLong()).format(formatter)
//        print(" $updateDate |")
//    }

}

