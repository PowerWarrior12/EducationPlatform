package com.example.educationplatform.domain.repositories

import com.example.educationplatform.domain.entities.User

interface UserRepositoryLocal {
    suspend fun saveUser(user: User): Result<Unit>
    suspend fun getUser(): Result<User>
    suspend fun getStatus(): String
}