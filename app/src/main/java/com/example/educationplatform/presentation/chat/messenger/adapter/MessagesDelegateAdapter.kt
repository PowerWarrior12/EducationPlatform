package com.example.educationplatform.presentation.chat.messenger.adapter

import com.bumptech.glide.Glide
import com.example.educationplatform.R
import com.example.educationplatform.databinding.MessageItemBinding
import com.example.educationplatform.databinding.UserMessageItemBinding
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun userMessageAdapterDelegate() =
    adapterDelegateViewBinding<MessageItem.UserMessageItem, MessageItem, MessageItemBinding>(
        { layoutInflater, root -> MessageItemBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.apply {
                Glide
                    .with(binding.root)
                    .load(item.message.userIcon)
                    .centerCrop()
                    .placeholder(R.drawable.student)
                    .into(icon)
                userName.text = item.message.userName
                messageText.text = item.message.text
            }
        }
    }

fun currentUserMessageAdapterDelegate() =
    adapterDelegateViewBinding<MessageItem.CurrentUserMessageItem, MessageItem, UserMessageItemBinding>(
        { layoutInflater, root -> UserMessageItemBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.apply {
                messageText.text = item.message.text
            }
        }
    }