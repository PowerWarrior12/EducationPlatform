package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.repositories.CoursesRepositoryRemote
import javax.inject.Inject

class CheckUserInCourseInteractor @Inject constructor(
    private val coursesRepositoryRemote: CoursesRepositoryRemote
) {
    suspend operator fun invoke(courseId: Int): Result<Boolean> {
        return try {
            coursesRepositoryRemote.isUserInCourse(courseId)
        } catch (e: java.lang.Exception) {
            return Result.failure(e)
        }
    }
}