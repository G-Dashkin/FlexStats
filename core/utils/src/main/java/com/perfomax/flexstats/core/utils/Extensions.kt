package com.perfomax.flexstats.core.utils

import com.perfomax.flexstats.models.Account
import com.perfomax.flexstats.models.User

fun String.addElement(newElement: String): String {
    return if(this.isEmpty()) "$this$newElement" else "$this|$newElement"
}

fun User.parsUserToString(): String {
    return "id:${this.id}|user:${this.user}|email:${this.email}|password:${this.password}"
}

fun String.parsStringToUser(): User {
    return User(id = this.split("|")[0].split(":")[1].toInt(),
                user = this.split("|")[1].split(":")[1],
                email = this.split("|")[2].split(":")[1],
                password = this.split("|")[3].split(":")[1],
                isLogin = false)
}

fun  List<Account>.yandexDirectFilter(): List<Account> {
    return this.filter { it.accountType == "yandex_direct" }
}

fun  List<Account>.yandexMetrikaFilter(): List<Account> {
    return this.filter { it.accountType == "yandex_metrika" }
}