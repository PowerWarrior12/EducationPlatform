package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.User
import com.example.educationplatform.domain.repositories.DeviceRepository
import com.example.educationplatform.domain.repositories.UserRepositoryRemote
import com.example.educationplatform.utils.ecxeptions.ConnectionException
import javax.inject.Inject

class RegistrationInteractor @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val userRepositoryRemote: UserRepositoryRemote
) {
    suspend operator fun invoke(user: User): Result<Unit> {
        return try {
            if (deviceRepository.checkInternetConnection()) {
                userRepositoryRemote.registration(user)
            } else {
                Result.failure(ConnectionException())
            }
        } catch (e: java.lang.Exception) {
            Result.failure(e)
        }
    }
}