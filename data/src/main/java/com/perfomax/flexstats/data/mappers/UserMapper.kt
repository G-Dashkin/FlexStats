package com.perfomax.flexstats.data.mappers

import com.perfomax.flexstats.models.User

internal fun String.toDomainUser(): User {
    return User(
        id = 0,
        user = this.split("user:")[1].split(";")[0],
        email = this.split("email:")[1].split(";")[0],
        password = this.split("password:")[1].split(";")[0]
    )
}