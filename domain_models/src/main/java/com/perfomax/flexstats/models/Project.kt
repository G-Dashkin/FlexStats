package com.perfomax.flexstats.models

data class Project(
    val id: Int? = null,
    val name: String,
    val isSelected: Boolean? = false,
    val userId: Int? = null
)