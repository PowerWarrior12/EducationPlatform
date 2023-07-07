package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.TakesModule
import com.example.educationplatform.domain.repositories.CoursesRepositoryRemote
import javax.inject.Inject

class LoadTakesModulesInteractor @Inject constructor(
    private val coursesRepositoryRemote: CoursesRepositoryRemote
) {
    suspend operator fun invoke(courseId: Int): Result<List<TakesModule>> {
        return try {
            coursesRepositoryRemote.getTakesModules(courseId)
        } catch (e: java.lang.Exception) {
            Result.failure(e)
        }
    }
}