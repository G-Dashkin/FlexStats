package com.perfomax.flexstats.projects.domain.usecases

import com.perfomax.flexstats.core.contracts.UseCaseWithoutParams
import com.perfomax.flexstats.data_api.repository.ProjectsRepository
import com.perfomax.flexstats.models.Project
import javax.inject.Inject

class GetSelectedProjectUseCase @Inject constructor(
    private val repository: ProjectsRepository
): UseCaseWithoutParams<Project> {
    override suspend fun execute(): Project {
        return repository.getSelected()
    }
}