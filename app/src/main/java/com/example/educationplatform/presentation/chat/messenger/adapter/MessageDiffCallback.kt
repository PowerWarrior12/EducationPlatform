package com.example.educationplatform.presentation.chat.messenger.adapter

import androidx.recyclerview.widget.DiffUtil

object MessageDiffCallback: DiffUtil.ItemCallback<MessageItem>() {
    override fun areItemsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean {
        return oldItem == newItem
    }
}