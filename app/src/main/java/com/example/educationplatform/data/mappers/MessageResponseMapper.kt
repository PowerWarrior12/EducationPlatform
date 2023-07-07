package com.example.educationplatform.data.mappers

import com.example.educationplatform.data.remote.chat.entities.MessageResponse
import com.example.educationplatform.domain.entities.Message

fun MessageResponse.toMessage(): Message {
    return Message(
        userName = userName,
        userIcon = "",
        text = text
    )
}