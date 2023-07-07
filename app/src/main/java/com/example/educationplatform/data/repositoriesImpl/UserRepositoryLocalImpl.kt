package com.example.educationplatform.data.repositoriesImpl

import com.example.educationplatform.data.local.PreferencesStorage
import com.example.educationplatform.data.local.RoomDB
import com.example.educationplatform.data.mappers.toUser
import com.example.educationplatform.data.mappers.toUserEntity
import com.example.educationplatform.domain.entities.User
import com.example.educationplatform.domain.repositories.UserRepositoryLocal
import javax.inject.Inject

private const val DB_ERROR = "Exception in database"
private const val NOT_USER_ERROR = "User haven't"

class UserRepositoryLocalImpl @Inject constructor(
    private val localDB: RoomDB,
    private val preferencesStorage: PreferencesStorage
): UserRepositoryLocal {
    override suspend fun saveUser(user: User): Result<Unit> {
        val users = localDB.userDao().getUsers()
        preferencesStorage.saveStatus(user.status)
        return try {
            if (users.isEmpty()) {
                localDB.userDao().createUser(user.toUserEntity())
            } else {
                val oldUser = users.first()
                localDB.userDao().updateUser(user.toUserEntity().copy(id = oldUser.id))
            }
            Result.success(Unit)
        } catch (e: java.lang.Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUser(): Result<User> {
        val users = localDB.userDao().getUsers()
        return if (users.isEmpty()) {
            Result.failure(java.lang.Exception(NOT_USER_ERROR))
        } else {
            Result.success(users.first().toUser())
        }
    }

    override suspend fun getStatus(): String {
        return preferencesStorage.getStatus() ?: ""
    }
}