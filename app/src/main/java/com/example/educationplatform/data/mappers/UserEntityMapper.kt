package com.example.educationplatform.data.mappers

import com.example.educationplatform.data.local.user.entities.UserEntity
import com.example.educationplatform.domain.entities.User

fun UserEntity.toUser(): User {
    return User(
        name = name,
        secondName = secondName,
        email = email,
        info = info,
        icon = icon,
        status = status
    )
}