package com.perfomax.flextats.data_api.storage

interface AuthStorage {
    suspend fun add(user: String)
    suspend fun getAll(): List<String>
    suspend fun setAuth(userName: String)
    suspend fun getAuth(): String
}