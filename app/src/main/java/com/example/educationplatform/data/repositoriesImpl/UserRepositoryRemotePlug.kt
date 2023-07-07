package com.example.educationplatform.data.repositoriesImpl

import com.example.educationplatform.domain.entities.User
import com.example.educationplatform.domain.repositories.UserRepositoryRemote
import kotlinx.coroutines.delay
import javax.inject.Inject

class UserRepositoryRemotePlug @Inject constructor(): UserRepositoryRemote {

    override suspend fun authorization(email: String, password: String): Result<User> {
        delay(2000)
        if (email.contains(".ru")) {
            return Result.failure(java.lang.Exception("Заблокированно"))
        } else return Result.success(User("", "", "", "", "", ""))
    }

    override suspend fun tokenAuthorization(token: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun registration(user: User): Result<Unit> {
        delay(2000)
        return Result.success(Unit)
    }

    override suspend fun updateUser(user: User): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun changeStatus(message: String): Result<Unit> {
        return Result.success(Unit)
    }
}