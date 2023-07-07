package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.repositories.UserRepositoryRemote
import javax.inject.Inject

class ChangeStatusInteractor @Inject constructor(
    private val userRepositoryRemote: UserRepositoryRemote
) {
    suspend operator fun invoke(message: String): Result<Unit> {
        return try {
            userRepositoryRemote.changeStatus(message)
        } catch (e: java.lang.Exception) {
            Result.failure(e)
        }
    }
}