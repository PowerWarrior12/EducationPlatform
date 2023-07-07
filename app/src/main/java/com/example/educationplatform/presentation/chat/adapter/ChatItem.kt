package com.example.educationplatform.presentation.chat.adapter

import com.example.educationplatform.domain.entities.Chat

sealed class ChatItem {
    class UserChat(val chat: Chat): ChatItem()
}