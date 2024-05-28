package com.perfomax.flexstats.projects.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithParams
import com.perfomax.flexstats.data_api.repository.AuthRepository
import com.perfomax.flexstats.data_api.repository.ProjectsRepository
import com.perfomax.flexstats.models.Project
import com.perfomax.flexstats.models.User
import javax.inject.Inject


class CreateProjectUseCase @Inject constructor(
    private val repository: ProjectsRepository
): UseCaseWithParams<Unit, Project> {
    override suspend fun execute(newProject: Project) {
        repository.create(newProject = newProject)
    }
}