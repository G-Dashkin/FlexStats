package com.perfomax.flexstats.data.mappers

import com.perfomax.flexstats.data.database.entities.UserEntity
import com.perfomax.flexstats.models.User

internal fun UserEntity.toDomain(): User {
    return User (
        id = id,
        email = email,
        user = user,
        password = password,
        isLogin = isLogin
    )
}

internal fun User.toDomain(): UserEntity {
    return UserEntity (
        id = 0,
        email = email,
        user = user,
        password = password,
        isLogin = isLogin
    )
}