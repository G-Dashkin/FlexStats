package com.perfomax.flexstats.models

data class Account(
    val id: Int? = 0,
    val name: String,
    val accountToken: String? = null,
    val password: String? = null,
    val projectId: Int? = 0
)