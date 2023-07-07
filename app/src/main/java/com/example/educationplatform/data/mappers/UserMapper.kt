package com.example.educationplatform.data.mappers

import com.example.educationplatform.data.local.user.entities.UserEntity
import com.example.educationplatform.data.remote.user.entities.EditUserRequest
import com.example.educationplatform.data.remote.user.entities.RegistrationRequest
import com.example.educationplatform.domain.entities.User

fun User.toEditUserRequest(): EditUserRequest {
    return EditUserRequest(
        name = name,
        secondName = secondName,
        email = email,
        password = password,
        info = info
    )
}

fun User.toRegistrationRequest(): RegistrationRequest {
    return RegistrationRequest(
        name = name,
        secondName = secondName,
        email = email,
        password = password
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        name = name,
        secondName = secondName,
        email = email,
        info = info,
        icon = icon,
        status = status
    )
}