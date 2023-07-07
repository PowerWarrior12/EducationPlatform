package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.repositories.CoursesRepositoryRemote
import com.example.educationplatform.domain.repositories.DeviceRepository
import javax.inject.Inject

class LoadDirectionsInteractor @Inject constructor(
    private val coursesRepositoryRemote: CoursesRepositoryRemote,
    private val deviceRepository: DeviceRepository) {
    suspend operator fun invoke(): Result<List<String>> {
        return try {
            if (deviceRepository.checkInternetConnection()) {
                coursesRepositoryRemote.getDirections()
            } else {
                Result.failure(java.lang.Exception())
            }
        } catch (e: java.lang.Exception) {
            Result.failure(e)
        }

    }
}