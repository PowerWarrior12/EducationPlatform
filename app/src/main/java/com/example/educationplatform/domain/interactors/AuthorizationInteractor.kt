package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.repositories.UserRepositoryLocal
import com.example.educationplatform.domain.repositories.UserRepositoryRemote
import javax.inject.Inject

class AuthorizationInteractor @Inject constructor(
    private val userRepositoryRemote: UserRepositoryRemote,
    private val userRepositoryLocal: UserRepositoryLocal
) {
    suspend fun run(email: String, password: String): Result<Unit> {
        val result = userRepositoryRemote.authorization(email, password)
        return try {
            userRepositoryLocal.saveToken(result.getOrThrow()).getOrThrow()
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}