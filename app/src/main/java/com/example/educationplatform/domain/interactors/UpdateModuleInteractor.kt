package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.Module
import com.example.educationplatform.domain.repositories.CoursesRepositoryRemote
import com.example.educationplatform.domain.repositories.DeviceRepository
import javax.inject.Inject

class UpdateModuleInteractor @Inject constructor(
    private val coursesRepositoryRemote: CoursesRepositoryRemote,
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(module: Module): Result<Unit> {
        return try {
            if (deviceRepository.checkInternetConnection()) {
                coursesRepositoryRemote.updateModule(module)
            } else {
                Result.failure(java.lang.Exception())
            }
        } catch (e: java.lang.Exception) {
            Result.failure(e)
        }
    }
}