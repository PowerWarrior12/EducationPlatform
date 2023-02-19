package com.example.educationplatform.data.repositoriesImpl

import com.example.educationplatform.domain.entities.User
import com.example.educationplatform.domain.repositories.UserRepositoryRemote
import javax.inject.Inject

class UserRepositoryRemotePlug @Inject constructor(): UserRepositoryRemote {

    override suspend fun authorization(email: String, password: String): Result<String> {
        if (email.contains(".ru")) {
            return Result.failure(java.lang.Exception("Заблокированно"))
        } else return Result.success("JSFDFJ43SFJIOE23FJSD12FSDFJ")
    }

    override suspend fun tokenAuthorization(token: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun registration(user: User): Result<Unit> {
        return Result.success(Unit)
    }
}