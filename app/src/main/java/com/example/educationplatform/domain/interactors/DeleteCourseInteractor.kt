package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.repositories.CoursesRepositoryRemote
import com.example.educationplatform.domain.repositories.DeviceRepository
import javax.inject.Inject

class DeleteCourseInteractor @Inject constructor(
    private val coursesRepositoryRemote: CoursesRepositoryRemote,
    private val deviceRepository: DeviceRepository
    ) {
    suspend operator fun invoke(courseId: Int): Result<Unit> {
        return try {
            if (deviceRepository.checkInternetConnection()) {
                coursesRepositoryRemote.deleteCourse(courseId)
            } else {
                Result.failure(java.lang.Exception())
            }
        } catch (e: java.lang.Exception) {
            Result.failure(e)
        }
    }
}