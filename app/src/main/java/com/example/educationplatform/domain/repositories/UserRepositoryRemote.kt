package com.example.educationplatform.domain.repositories

import com.example.educationplatform.domain.entities.User

interface UserRepositoryRemote {
    suspend fun authorization(email: String, password: String): Result<String>
    suspend fun tokenAuthorization(token: String): Result<Unit>
    suspend fun registration(user: User): Result<Unit>
}