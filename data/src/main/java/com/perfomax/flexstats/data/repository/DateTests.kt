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
    println("  startUpdateDate: $startUpdateDate")
    println("  firstUpdateDate: $firstUpdateDate")
    println("        yesterday: $yesterday")

    // 3) Вычисление количества дней между вчерашнем днем и датой перого обновления базы (минус 1 месяц)
    val secondTimestampExclusive = LocalDate.of(
        yesterday.split("-")[0].toInt(),
        yesterday.split("-")[1].toInt(),
        yesterday.split("-")[2].toInt(),
    )
    println("secondTimestampExclusive: $secondTimestampExclusive")
    val numberBetweenDays_FirstDay = Duration.between(
        startUpdateDate.atStartOfDay(),
        secondTimestampExclusive.atStartOfDay())
        .toDays()
        .toInt()
        .inc()
    println("numberBetweenDays_FirstDay: $numberBetweenDays_FirstDay")

    // тут нужно получить кличество дней от сегодняшней даты минус дата начала обновления с прошолго месяца
    // а тут кличество дней от сегодняшней даты минус дата последнего обновления

    val lastDay = 47 // МНЕ НУЖНО!!! получить это число
    val firstDay = 77

    for (day in lastDay ..numberBetweenDays_FirstDay){
        // функция получет дату с минусом колучества дней от сегодняцшнего дня.
        val updateDate = LocalDateTime.now().minusDays(day.toLong()).format(formatter)
        print(" $updateDate |")
    }

}

