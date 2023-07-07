package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.Module
import com.example.educationplatform.domain.repositories.CoursesRepositoryRemote
import com.example.educationplatform.domain.repositories.DeviceRepository
import javax.inject.Inject

class CreateModuleInteractor @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val coursesRepositoryRemote: CoursesRepositoryRemote
) {
    suspend operator fun invoke(module: Module): Result<Int> {
        return try {
            if (deviceRepository.checkInternetConnection()) {
                coursesRepositoryRemote.createModule(module)
            } else {
                Result.failure(java.lang.Exception())
            }
        } catch (e: java.lang.Exception) {
            Result.failure(e)
        }
    }
}