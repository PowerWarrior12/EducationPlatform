package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.Stage
import com.example.educationplatform.domain.repositories.CoursesRepositoryRemote
import com.example.educationplatform.domain.repositories.DeviceRepository
import javax.inject.Inject

class CreateStageInteractor @Inject constructor(
    private val coursesRepositoryRemote: CoursesRepositoryRemote,
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(stage: Stage): Result<Int> {
        return try {
            if (deviceRepository.checkInternetConnection()) {
                coursesRepositoryRemote.createStage(stage)
            } else {
                Result.failure(java.lang.Exception())
            }
        } catch (e: java.lang.Exception) {
            Result.failure(e)
        }
    }
}