package com.example.educationplatform.domain.repositories

import com.example.educationplatform.domain.entities.Chat
import com.example.educationplatform.domain.entities.Message
import com.example.educationplatform.domain.entities.SentMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepositoryRemote {
    suspend fun getChatsByUser(): Result<List<Chat>>
    suspend fun getMessagesByChat(chatId: Int): Flow<List<Message>>
    suspend fun createMessage(message: SentMessage): Result<Unit>
}