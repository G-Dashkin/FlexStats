package com.perfomax.flexstats.core.utils

import androidx.fragment.app.FragmentManager
import com.perfomax.flexstats.core.contracts.DATE_FORMAT
import com.perfomax.flexstats.models.Account
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.util.Locale

fun List<Account>.yandexDirectFilter(): List<Account> {
    return this.filter { it.accountType == "yandex_direct" }
}

fun List<Account>.yandexMetrikaFilter(): List<Account> {
    return this.filter { it.accountType == "yandex_metrika" }
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


fun String.toTimestamp (minusDays: Int = 0): Long {
    val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    val date = simpleDateFormat.parse(this)
    return if (minusDays != 0) date.time.minus(minusDays) else date?.time?:0
}

fun Pair<String, String>.toDateList(): List<String> {
    val startDate = LocalDate.of(
        this.first.split("-")[0].toInt(),
        this.first.split("-")[1].toInt(),
        this.first.split("-")[2].toInt(),
    )
    val endDate = LocalDate.of(
        this.second.split("-")[0].toInt(),
        this.second.split("-")[1].toInt(),
        this.second.split("-")[2].toInt(),
    )
    val dates = mutableListOf<String>()
    var currentDate = startDate
    while (currentDate.isBefore(endDate.plusDays(1))) {
        dates.add(currentDate.toString())
        currentDate = currentDate.plusDays(1)
    }
    return dates
}

fun FragmentManager.getFragmentName():String {
    return this.fragments.get(0).toString().split("{")[0]
}
