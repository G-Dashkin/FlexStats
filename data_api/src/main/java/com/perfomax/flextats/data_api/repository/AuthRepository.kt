package com.perfomax.flextats.data_api.repository

import com.perfomax.flexstats.models.User

interface AuthRepository {
    suspend fun create(newUser: String)
    suspend fun getAll(): List<User>
    suspend fun setAuth(userName: String)
    suspend fun getAuth(): String
}