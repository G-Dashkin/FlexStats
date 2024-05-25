package com.perfomax.flextats.data_api.datastore

interface AuthDataStore {
    suspend fun setUser(user: String)
    suspend fun getUsers(): String
    suspend fun setAuth(userName: String)
    suspend fun getAuth(): String
}