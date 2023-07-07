package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.TakesStage
import com.example.educationplatform.domain.repositories.CoursesRepositoryRemote
import javax.inject.Inject

class UpdateTakesStageInteractor @Inject constructor(
    private val coursesRepositoryRemote: CoursesRepositoryRemote
) {
    suspend operator fun invoke(stage: TakesStage): Result<Unit> {
        return try {
            coursesRepositoryRemote.updateTakesStage(stage)
        } catch (e: java.lang.Exception) {
            Result.failure(e)
        }
    }
}