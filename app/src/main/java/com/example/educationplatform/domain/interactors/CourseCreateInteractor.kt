package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.Course
import com.example.educationplatform.domain.repositories.CoursesRepositoryRemote
import com.example.educationplatform.domain.repositories.DeviceRepository
import com.example.educationplatform.utils.ecxeptions.ConnectionException
import javax.inject.Inject

class CourseCreateInteractor @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val coursesRepositoryRemote: CoursesRepositoryRemote
) {
    suspend operator fun invoke(course: Course): Result<Int> {
        return try {
            if (deviceRepository.checkInternetConnection()) {
                coursesRepositoryRemote.createCourse(course)
            } else {
                Result.failure(ConnectionException())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}