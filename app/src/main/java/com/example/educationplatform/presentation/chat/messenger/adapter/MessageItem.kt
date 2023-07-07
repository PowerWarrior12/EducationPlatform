package com.example.educationplatform.presentation.chat.messenger.adapter

import com.example.educationplatform.domain.entities.Message

sealed class MessageItem {
    class UserMessageItem(val message: Message): MessageItem()
    class CurrentUserMessageItem(val message: Message): MessageItem()
}
