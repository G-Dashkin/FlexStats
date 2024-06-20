package com.perfomax.flexstats.data.mappers

import com.perfomax.flexstats.core.utils.EMPTY
import com.perfomax.flexstats.data.database.entities.AccountEntity
import com.perfomax.flexstats.models.Account

internal fun AccountEntity.toDomain(): Account {
    return Account (
        id = id,
        name = name,
        accountToken = token,
        accountType = type,
        metrikaCounter = metrikaCounter,
        projectId = projectId
    )
}

internal fun Account.toDomain(): AccountEntity {
    return AccountEntity (
        id = id?:0,
        name = name,
        token = accountToken?: EMPTY,
        type = accountType?:EMPTY,
        metrikaCounter = metrikaCounter?:EMPTY,
        projectId = projectId?:0,
    )
}