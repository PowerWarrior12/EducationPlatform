package com.example.educationplatform.presentation.chat.messenger.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class MessagesAdapter: AsyncListDifferDelegationAdapter<MessageItem>(
    MessageDiffCallback,
    userMessageAdapterDelegate(),
    currentUserMessageAdapterDelegate()
)