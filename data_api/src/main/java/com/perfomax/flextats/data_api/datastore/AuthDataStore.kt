package com.perfomax.flextats.data_api.datastore

import com.perfomax.flexstats.models.User

interface AuthDataStore {
    suspend fun setAuth(authUser: User)
    suspend fun getAuth(): User
}