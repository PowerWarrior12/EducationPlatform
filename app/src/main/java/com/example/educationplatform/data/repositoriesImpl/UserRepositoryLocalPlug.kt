package com.example.educationplatform.data.repositoriesImpl

import com.example.educationplatform.domain.entities.User
import com.example.educationplatform.domain.repositories.UserRepositoryLocal
import javax.inject.Inject

class UserRepositoryLocalPlug @Inject constructor(): UserRepositoryLocal {
    override suspend fun saveUser(user: User): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getUser(): Result<User> {
        return Result.success(User("Peter", "Uhlimov", "jujhjl34", "businessmail1710@mail.ru"))
    }

    override suspend fun getStatus(): String {
        return ""
    }
}