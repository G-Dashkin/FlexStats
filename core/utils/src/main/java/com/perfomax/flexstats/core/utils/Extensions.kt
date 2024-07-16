package com.perfomax.flexstats.core.utils

import androidx.fragment.app.FragmentManager
import com.perfomax.flexstats.models.Account

fun List<Account>.yandexDirectFilter(): List<Account> {
    return this.filter { it.accountType == "yandex_direct" }
}

fun List<Account>.yandexMetrikaFilter(): List<Account> {
    return this.filter { it.accountType == "yandex_metrika" }
}

fun FragmentManager.getFragmentName():String {
    return this.fragments.get(0).toString().split("{")[0]
}