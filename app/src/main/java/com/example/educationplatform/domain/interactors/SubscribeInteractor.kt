package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.repositories.CoursesRepositoryRemote
import javax.inject.Inject

class SubscribeInteractor @Inject constructor(
    private val coursesRepositoryRemote: CoursesRepositoryRemote
) {
    suspend operator fun invoke(currentSubscription: Boolean, courseId: Int): Result<Unit> {
        return try {
            if (currentSubscription) {
                coursesRepositoryRemote.unsubscribe(courseId)
            } else {
                coursesRepositoryRemote.subscribe(courseId)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}