package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.Module
import com.example.educationplatform.domain.repositories.CoursesRepositoryRemote
import com.example.educationplatform.domain.repositories.DeviceRepository
import javax.inject.Inject

class LoadCourseModulesInteractor @Inject constructor(private val coursesRepositoryRemote: CoursesRepositoryRemote,
private val deviceRepository: DeviceRepository) {
    suspend operator fun invoke(courseId: Int): Result<List<Module>> {
        return try {
            if (deviceRepository.checkInternetConnection()) {
                coursesRepositoryRemote.getModulesByCourseId(courseId)
            } else {
                Result.failure(java.lang.Exception())
            }
        } catch (e: java.lang.Exception) {
            Result.failure(e)
        }
    }
}