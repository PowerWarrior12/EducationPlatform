package com.example.educationplatform.data.remote.user

import com.example.educationplatform.data.remote.user.entities.*
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @POST("user/registration")
    suspend fun registration(@Body request: RegistrationRequest): Response<Void>

    @POST("user/authenticated")
    suspend fun authorization(@Body request: AuthorizationRequest): Response<UserResponse>

    @PUT("user/edit")
    suspend fun updateUser(@Body request: EditUserRequest): Response<Void>

    @POST("user/changeStatus")
    suspend fun changeStatus(@Body request: ChangeStatusRequest): Response<Void>
}