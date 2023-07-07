package com.example.educationplatform.data.mappers

import com.example.educationplatform.data.remote.chat.entities.ChatResponse
import com.example.educationplatform.domain.entities.Chat
import com.example.educationplatform.domain.entities.Message

fun ChatResponse.toChat(): Chat {
    return Chat(
        id = id,
        title = title,
        icon = icon,
        lastMessage = lastMessage?.toMessage() ?: Message.empty()
    )
}