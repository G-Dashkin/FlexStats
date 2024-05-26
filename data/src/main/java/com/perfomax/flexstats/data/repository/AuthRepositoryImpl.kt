package com.perfomax.flexstats.data.repository

import com.perfomax.flexstats.models.User
import com.perfomax.flextats.data_api.repository.AuthRepository
import com.perfomax.flextats.data_api.storage.AuthStorage
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authStorage: AuthStorage
): AuthRepository {
    override suspend fun create(newUser: User) {
        authStorage.add(newUser)
    }

    override suspend fun getAll(): List<User> {
        return authStorage.getAllUsers()
    }

    override suspend fun setAuth(userName: String) {
        authStorage.setAuth(userName = userName)
    }

    override suspend fun getAuth(): String {
        return authStorage.getAuth()
    }
}