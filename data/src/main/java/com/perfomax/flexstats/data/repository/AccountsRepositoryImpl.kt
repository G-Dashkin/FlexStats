package com.perfomax.flexstats.data.repository

import com.perfomax.flexstats.data_api.repository.AccountsRepository
import com.perfomax.flexstats.data_api.storage.AccountsStorage
import com.perfomax.flexstats.data_api.storage.AuthStorage
import com.perfomax.flexstats.data_api.storage.ProjectsStorage
import com.perfomax.flexstats.models.Account
import com.perfomax.flexstats.models.Project
import javax.inject.Inject

class AccountsRepositoryImpl @Inject constructor(
    private val accountsStorage: AccountsStorage,
    private val projectsStorage: ProjectsStorage,
    private val authStorage: AuthStorage
): AccountsRepository {
    override suspend fun create(account: Account) {
        val authedUserId = authStorage.getAuthUser().id
        val selectedProject = projectsStorage.getSelectedProject(userId = authedUserId?:0)
        accountsStorage.add(Account(name = account.name, projectId = selectedProject.id, token = account.token))
    }

    override suspend fun getUserAll(): List<Account> {
        val authedUserId = authStorage.getAuthUser().id
        val selectedProject = projectsStorage.getSelectedProject(userId = authedUserId?:0)
        return accountsStorage.getAllAccountsOfUser(projectId = selectedProject.id?:0)
    }
}