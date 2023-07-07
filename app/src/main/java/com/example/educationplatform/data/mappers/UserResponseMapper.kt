package com.example.educationplatform.data.mappers

import com.example.educationplatform.data.remote.user.entities.UserResponse
import com.example.educationplatform.domain.entities.User

fun UserResponse.toUser(): User {
    return User(
        name = name,
        secondName = secondName,
        email = email,
        info = info,
        status = status
    )
}