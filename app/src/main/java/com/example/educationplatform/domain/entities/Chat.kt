package com.example.educationplatform.domain.entities

data class Chat(
    val id: Int,
    val title: String,
    val icon: String,
    val lastMessage: Message
)