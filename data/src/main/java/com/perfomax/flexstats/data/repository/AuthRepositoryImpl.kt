package com.perfomax.flexstats.data.repository

import com.perfomax.flexstats.models.User
import com.perfomax.flexstats.data_api.repository.AuthRepository
import com.perfomax.flexstats.data_api.storage.AuthStorage
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

    override suspend fun setAuth() {
        authStorage.setAuth()
    }

    override suspend fun getAuth(): Boolean {
        return authStorage.getAuth()
    }
}