package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.User
import com.example.educationplatform.domain.repositories.UserRepositoryLocal
import javax.inject.Inject

class LoadCurrentUserInteractor @Inject constructor(private val userRepositoryLocal: UserRepositoryLocal) {
    suspend operator fun invoke(): Result<User> {
        return userRepositoryLocal.getUser()
    }
}