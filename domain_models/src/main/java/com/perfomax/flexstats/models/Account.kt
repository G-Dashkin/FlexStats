package com.perfomax.flexstats.models

data class Account(
    val id: Int? = 0,
    val name: String,
    val token: String,
    val projectId: Int? = 0
)