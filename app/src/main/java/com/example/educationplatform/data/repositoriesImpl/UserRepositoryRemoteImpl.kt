package com.example.educationplatform.data.repositoriesImpl

import com.example.educationplatform.data.mappers.toEditUserRequest
import com.example.educationplatform.data.mappers.toRegistrationRequest
import com.example.educationplatform.data.mappers.toUser
import com.example.educationplatform.data.remote.user.UserApi
import com.example.educationplatform.data.remote.user.entities.AuthorizationRequest
import com.example.educationplatform.data.remote.user.entities.ChangeStatusRequest
import com.example.educationplatform.domain.entities.User
import com.example.educationplatform.domain.repositories.UserRepositoryRemote
import javax.inject.Inject

class UserRepositoryRemoteImpl @Inject constructor(
    private val userApi: UserApi
): UserRepositoryRemote {
    override suspend fun authorization(email: String, password: String): Result<User> {
        val response = userApi.authorization(AuthorizationRequest(email, password))
        return if (response.isSuccessful) {
            Result.success(response.body()!!.toUser())
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun tokenAuthorization(token: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun registration(user: User): Result<Unit> {
        val response = userApi.registration(user.toRegistrationRequest())
        return if(response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun updateUser(user: User): Result<Unit> {
        val response = userApi.updateUser(user.toEditUserRequest())
        return if(response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun changeStatus(message: String): Result<Unit> {
        val response = userApi.changeStatus(ChangeStatusRequest(message))
        return if(response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message()))
        }
    }
}