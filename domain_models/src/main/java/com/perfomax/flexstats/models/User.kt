package com.perfomax.flexstats.models

data class User(
    val id: Int? = null,
    val user: String,
    val email: String,
    val password: String
)