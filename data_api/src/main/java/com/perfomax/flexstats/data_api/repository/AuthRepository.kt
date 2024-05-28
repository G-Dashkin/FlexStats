package com.perfomax.flexstats.data_api.repository

import com.perfomax.flexstats.models.User

interface AuthRepository {
    suspend fun create(newUser: User)
    suspend fun getAll(): List<User>
    suspend fun setAuth()
    suspend fun getAuth(): Boolean
}