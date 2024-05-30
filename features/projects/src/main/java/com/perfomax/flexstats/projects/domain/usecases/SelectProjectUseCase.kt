package com.perfomax.flexstats.projects.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithParams
import com.perfomax.flexstats.data_api.repository.ProjectsRepository
import com.perfomax.flexstats.models.Project
import javax.inject.Inject

class SelectProjectUseCase @Inject constructor(
    private val repository: ProjectsRepository
): UseCaseWithParams<Unit, Int> {
    override suspend fun execute(projectId: Int) {
        repository.select(project = projectId)
    }
}