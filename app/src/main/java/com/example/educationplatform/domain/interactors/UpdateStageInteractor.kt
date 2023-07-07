package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.Stage
import com.example.educationplatform.domain.repositories.CoursesRepositoryRemote
import com.example.educationplatform.domain.repositories.DeviceRepository
import javax.inject.Inject

class UpdateStageInteractor @Inject constructor(
    private val coursesRepositoryRemote: CoursesRepositoryRemote,
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(stage: Stage): Result<Unit> {
        return try {
            if (deviceRepository.checkInternetConnection()) {
                coursesRepositoryRemote.updateStage(stage)
            } else {
                Result.failure(java.lang.Exception())
            }
        } catch (e: java.lang.Exception) {
            Result.failure(e)
        }
    }
}