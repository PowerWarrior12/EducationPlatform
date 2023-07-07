package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.SubCourse
import com.example.educationplatform.domain.repositories.CoursesRepositoryRemote
import com.example.educationplatform.domain.repositories.DeviceRepository
import javax.inject.Inject

class LoadTakesCoursesInteractor @Inject constructor(
    private val coursesRepositoryRemote: CoursesRepositoryRemote,
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(): Result<List<SubCourse>> {
        return try {
            if (deviceRepository.checkInternetConnection()) {
                coursesRepositoryRemote.getTakesCourses()
            } else {
                Result.failure(Exception())
            }
        } catch (e: java.lang.Exception) {
            Result.failure(e)
        }
    }
}