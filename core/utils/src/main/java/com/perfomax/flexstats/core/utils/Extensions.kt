package com.perfomax.flexstats.core.utils

fun String.addElement(newElement: String): String {
    return if(this.isEmpty()) "$this$newElement" else "$this|$newElement"
}


fun String.parsToList(): List<String> {
    return this.split("|")
}