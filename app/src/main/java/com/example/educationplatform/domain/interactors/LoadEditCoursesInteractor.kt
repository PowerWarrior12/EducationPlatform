package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.UsersCourse
import com.example.educationplatform.domain.repositories.CoursesRepositoryRemote
import com.example.educationplatform.domain.repositories.DeviceRepository
import javax.inject.Inject

class LoadEditCoursesInteractor @Inject constructor(
    private val coursesRepositoryRemote: CoursesRepositoryRemote,
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(): Result<List<UsersCourse>> {
        return try {
            if (deviceRepository.checkInternetConnection()) {
                coursesRepositoryRemote.getEditCourses()
            } else {
                Result.failure(java.lang.Exception())
            }
        } catch (e: java.lang.Exception) {
            Result.failure(e)
        }
    }
}