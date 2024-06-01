package com.perfomax.flexstats.projects.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithoutParams
import com.perfomax.flexstats.data_api.repository.ProjectsRepository
import com.perfomax.flexstats.models.Project
import javax.inject.Inject

class GetUserProjectsUseCase @Inject constructor(
    private val repository: ProjectsRepository
): UseCaseWithoutParams<List<Project>> {
    override suspend fun execute(): List<Project> {
        return repository.getUserAll()
    }
}