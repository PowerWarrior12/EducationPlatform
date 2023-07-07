package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.repositories.UserRepositoryLocal
import javax.inject.Inject

class GetStatusInteractor @Inject constructor(
    private val userRepositoryLocal: UserRepositoryLocal
) {
    suspend operator fun invoke(): String {
        return userRepositoryLocal.getStatus()
    }
}