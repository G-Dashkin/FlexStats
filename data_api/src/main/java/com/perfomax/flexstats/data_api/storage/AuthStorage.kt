package com.perfomax.flexstats.data_api.storage

import com.perfomax.flexstats.models.User

interface AuthStorage {
    suspend fun add(user: User)
    suspend fun getAllUsers(): List<User>
    suspend fun setAuth()
    suspend fun getAuth(): Boolean
}