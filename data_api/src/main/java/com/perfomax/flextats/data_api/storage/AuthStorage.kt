package com.perfomax.flextats.data_api.storage

import com.perfomax.flexstats.models.User

interface AuthStorage {
    suspend fun add(user: User)
    suspend fun getAllUsers(): List<User>
    suspend fun setAuth(userName: String)
    suspend fun getAuth(): String
}