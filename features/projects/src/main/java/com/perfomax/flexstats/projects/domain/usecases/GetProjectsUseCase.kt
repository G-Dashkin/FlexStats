package com.perfomax.flexstats.projects.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithoutParams
import com.perfomax.flexstats.data_api.repository.AuthRepository
import com.perfomax.flexstats.data_api.repository.ProjectsRepository
import com.perfomax.flexstats.models.Project
import com.perfomax.flexstats.models.User
import javax.inject.Inject

class GetProjectsUseCase @Inject constructor(
    private val repository: ProjectsRepository
): UseCaseWithoutParams<List<Project>> {
    override suspend fun execute(): List<Project> {
        return repository.getAll()
    }
}