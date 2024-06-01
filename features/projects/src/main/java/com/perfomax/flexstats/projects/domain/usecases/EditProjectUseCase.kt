package com.perfomax.flexstats.projects.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithParams
import com.perfomax.flexstats.data_api.repository.ProjectsRepository
import com.perfomax.flexstats.models.Project
import javax.inject.Inject

class EditProjectUseCase @Inject constructor(
    private val repository: ProjectsRepository
): UseCaseWithParams<Unit, Project> {
    override suspend fun execute(project: Project) {
        repository.edit(projectId = project.id?:0, editName = project.name)
    }
}