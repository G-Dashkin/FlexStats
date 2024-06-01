package com.perfomax.flexstats.models

data class Project(
    val id: Int? = 0,
    val name: String,
    val isSelected: Boolean? = false,
    val userId: Int? = 0
)