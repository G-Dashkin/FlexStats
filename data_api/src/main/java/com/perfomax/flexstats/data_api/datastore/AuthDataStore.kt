package com.perfomax.flexstats.data_api.datastore

import com.perfomax.flexstats.models.User

interface AuthDataStore {
    suspend fun setAuthUser(authUser: User)
    suspend fun getAuthUser(): User
    suspend fun setAuth()
    suspend fun getAuth(): Boolean
    suspend fun logout()
}