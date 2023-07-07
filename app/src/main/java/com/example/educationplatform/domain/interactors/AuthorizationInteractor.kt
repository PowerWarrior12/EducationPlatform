package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.repositories.DeviceRepository
import com.example.educationplatform.domain.repositories.UserRepositoryLocal
import com.example.educationplatform.domain.repositories.UserRepositoryRemote
import com.example.educationplatform.utils.ecxeptions.ConnectionException
import javax.inject.Inject

class AuthorizationInteractor @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val userRepositoryRemote: UserRepositoryRemote,
    private val userRepositoryLocal: UserRepositoryLocal
) {
    suspend fun run(email: String, password: String): Result<Unit> {
        return try {
            if (deviceRepository.checkInternetConnection()) {
                userRepositoryRemote.authorization(email, password).onSuccess { user ->
                    userRepositoryLocal.saveUser(user)
                }.map {}
            } else {
                Result.failure(ConnectionException())
            }
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}