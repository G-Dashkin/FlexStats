package com.perfomax.flexstats.core.utils

import androidx.fragment.app.FragmentManager
import com.perfomax.flexstats.core.contracts.DATE_FORMAT
import com.perfomax.flexstats.models.Account
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun List<Account>.yandexDirectFilter(): List<Account> {
    return this.filter { it.accountType == "yandex_direct" }
}

fun List<Account>.yandexMetrikaFilter(): List<Account> {
    return this.filter { it.accountType == "yandex_metrika" }
}

fun String.dateMinusDays(minusDays: Int = 0) : String{
    val date = LocalDate.parse(this)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val newDate = date.minusDays(minusDays.toLong())
    return newDate.format(formatter)
}

fun String.isNotMaxUpdateDate(maxUpdateDate: Int): Boolean {
    val checkedDate = LocalDate.parse(this)
    val today = LocalDate.now()
    val sixMonthsAgo = today.minusMonths(maxUpdateDate.toLong()-1)
    val firstDateSixMonthsAgo = sixMonthsAgo.withDayOfMonth(1)
    return checkedDate != firstDateSixMonthsAgo
}

fun String.getDaysDiapason(diapasonDate: String): Int {
    val firstTimestampInclusive = LocalDate.of(
        this.split("-")[0].toInt(),
        this.split("-")[1].toInt(),
        this.split("-")[2].toInt(),
    )
    val secondTimestampExclusive = LocalDate.of(
        diapasonDate.split("-")[0].toInt(),
        diapasonDate.split("-")[1].toInt(),
        diapasonDate.split("-")[2].toInt(),
    )
    val numberOfDays = Duration.between(
        firstTimestampInclusive.atStartOfDay(),
        secondTimestampExclusive.atStartOfDay())
        .toDays()
        .toInt()
        .inc()
    return numberOfDays
}


fun String.datePlusDays(minusDays: Int = 0) : String{
    val date = LocalDate.parse(this)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val newDate = date.plusDays(minusDays.toLong())
    return newDate.format(formatter)
}

fun String.toTimestamp (minusDays: Int = 0): Long {
    val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    val date = simpleDateFormat.parse(this)
    return if (minusDays != 0) date.time.minus(minusDays) else date?.time?:0
}

fun Long.toDateString (minusDays: Int = 0): String {
    val simpleDate = SimpleDateFormat("yyyy-MM-dd")
    val calendar = Calendar.getInstance()
    //--------------------------------------------------
    val date = Date(this)
    val stringDate = simpleDate.format(date)
    //--------------------------------------------------
    calendar.time = Date(this)
    calendar.add(Calendar.DAY_OF_YEAR, -minusDays) // Subtract 5 days
    val newTimestamp = calendar.time.time
    val simpleDateMinusDays = Date(newTimestamp)
    val stringDateMinusDays = simpleDate.format(simpleDateMinusDays)
    return if (minusDays != 0) stringDateMinusDays else stringDate
}

fun FragmentManager.getFragmentName():String {
    return this.fragments.get(0).toString().split("{")[0]
}

