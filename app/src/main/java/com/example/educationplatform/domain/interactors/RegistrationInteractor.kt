package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.User
import com.example.educationplatform.domain.repositories.UserRepositoryRemote
import javax.inject.Inject

class RegistrationInteractor @Inject constructor(
    private val userRepositoryRemote: UserRepositoryRemote
) {

    suspend fun run(user: User): Result<Unit> {
        return userRepositoryRemote.registration(user)
    }
}