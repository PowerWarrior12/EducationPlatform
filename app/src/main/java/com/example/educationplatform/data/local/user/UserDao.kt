package com.example.educationplatform.data.local.user

import androidx.room.*
import com.example.educationplatform.data.local.user.entities.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun createUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity): Int

    @Transaction
    @Query("SELECT * FROM user")
    suspend fun getUsers(): List<UserEntity>

    @Delete
    suspend fun deleteUser(user: UserEntity): Int
}