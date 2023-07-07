package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.User
import com.example.educationplatform.domain.repositories.DeviceRepository
import com.example.educationplatform.domain.repositories.UserRepositoryLocal
import com.example.educationplatform.domain.repositories.UserRepositoryRemote
import com.example.educationplatform.utils.ecxeptions.ConnectionException
import javax.inject.Inject

class UpdateUserInteractor @Inject constructor(
    private val userRepositoryLocal: UserRepositoryLocal,
    private val userRepositoryRemote: UserRepositoryRemote,
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(user: User): Result<Unit> {
        return try {
            if (deviceRepository.checkInternetConnection()) {
                userRepositoryRemote.updateUser(user).onSuccess {
                    userRepositoryLocal.saveUser(user).onSuccess {
                        Result.success(Unit)
                    }
                }
            } else {
                Result.failure(ConnectionException())
            }
        }  catch (e: java.lang.Exception) {
            Result.failure(e)
        }
    }
}