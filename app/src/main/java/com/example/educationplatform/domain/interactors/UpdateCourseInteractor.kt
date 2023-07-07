package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.Course
import com.example.educationplatform.domain.repositories.CoursesRepositoryRemote
import com.example.educationplatform.domain.repositories.DeviceRepository
import javax.inject.Inject

class UpdateCourseInteractor @Inject constructor(
    private val coursesRepositoryRemote: CoursesRepositoryRemote,
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(course: Course): Result<Unit> {
        return try {
            if (deviceRepository.checkInternetConnection()) {
                coursesRepositoryRemote.updateCourse(course)
            } else {
                Result.failure(java.lang.Exception())
            }
        } catch (e: java.lang.Exception) {
            Result.failure(e)
        }
    }
}