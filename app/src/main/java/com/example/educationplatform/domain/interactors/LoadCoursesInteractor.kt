package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.Course
import com.example.educationplatform.domain.repositories.CoursesRepositoryRemote
import com.example.educationplatform.domain.repositories.DeviceRepository
import javax.inject.Inject

class LoadCoursesInteractor @Inject constructor(private val coursesRepositoryRemote: CoursesRepositoryRemote,
private val deviceRepository: DeviceRepository) {
    suspend operator fun invoke(): Result<List<Course>> {
        return try {
            if (deviceRepository.checkInternetConnection()) {
                coursesRepositoryRemote.getCourses()
            } else {
                Result.failure(Exception())
            }
        } catch (e: java.lang.Exception) {
            Result.failure(e)
        }

    }
}