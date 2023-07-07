package com.example.educationplatform.presentation.chat.adapter

import com.bumptech.glide.Glide
import com.example.educationplatform.R
import com.example.educationplatform.databinding.ChatItemBinding
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun userChatAdapterDelegate(onChatClickListener: ((chatId: Int) -> Unit)) =
    adapterDelegateViewBinding<ChatItem.UserChat, ChatItem, ChatItemBinding>(
        { layoutInflater, root -> ChatItemBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.apply {
                Glide
                    .with(binding.root)
                    .load(item.chat.icon)
                    .centerCrop()
                    .placeholder(R.drawable.student)
                    .into(binding.icon)
                userName.text = item.chat.lastMessage.userName
                chatTitle.text = item.chat.title
                messageText.text = item.chat.lastMessage.text
                root.setOnClickListener {
                    onChatClickListener(item.chat.id)
                }
            }
        }
    }