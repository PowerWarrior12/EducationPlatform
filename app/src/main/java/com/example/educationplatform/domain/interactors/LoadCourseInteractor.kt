package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.Course
import com.example.educationplatform.domain.repositories.CoursesRepositoryRemote
import com.example.educationplatform.domain.repositories.DeviceRepository
import javax.inject.Inject

class LoadCourseInteractor @Inject constructor(
    private val coursesRepositoryRemote: CoursesRepositoryRemote,
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(courseId: Int): Result<Course> {
        return try {
            if (deviceRepository.checkInternetConnection()) {
                coursesRepositoryRemote.getFullCourseInfo(courseId)
            } else {
                Result.failure(Exception())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}