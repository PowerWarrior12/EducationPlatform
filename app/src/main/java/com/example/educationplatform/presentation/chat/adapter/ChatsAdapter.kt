package com.example.educationplatform.presentation.chat.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class ChatsAdapter(
    onChatClick: (chatId: Int) -> Unit
): AsyncListDifferDelegationAdapter<ChatItem>(
    ChatDiffCallback,
    userChatAdapterDelegate(onChatClick)
)