package com.example.educationplatform.data.mappers

import com.example.educationplatform.data.remote.chat.entities.CreateMessageRequest
import com.example.educationplatform.domain.entities.SentMessage

fun SentMessage.toCreateMessageRequest(): CreateMessageRequest {
    return CreateMessageRequest(
        chatId = chatId,
        text = text
    )
}