package com.example.educationplatform.data.repositoriesImpl

import com.example.educationplatform.data.mappers.toChat
import com.example.educationplatform.data.mappers.toCreateMessageRequest
import com.example.educationplatform.data.mappers.toMessage
import com.example.educationplatform.data.remote.chat.ChatApi
import com.example.educationplatform.data.remote.chat.MessagesService
import com.example.educationplatform.domain.entities.Chat
import com.example.educationplatform.domain.entities.Message
import com.example.educationplatform.domain.entities.SentMessage
import com.example.educationplatform.domain.repositories.ChatRepositoryRemote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatRepositoryRemoteImpl @Inject constructor(
    private val messagesService: MessagesService,
    private val chatApi: ChatApi
): ChatRepositoryRemote {
    override suspend fun getChatsByUser(): Result<List<Chat>> {
        val response = chatApi.getUserChats()
        return if (response.isSuccessful) {
            Result.success(response.body()!!.map { chatResponse ->
                chatResponse.toChat()
            })
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override suspend fun getMessagesByChat(chatId: Int): Flow<List<Message>> {
        val messagesFlow = MutableSharedFlow<List<Message>>()
        messagesService.getChatMessages(chatId) { messagesResponse ->
            CoroutineScope(Dispatchers.IO).launch {
                messagesFlow.emit(messagesResponse.messages.map { messageResponse ->
                    messageResponse.toMessage()
                })
            }
        }
        return messagesFlow
    }

    override suspend fun createMessage(message: SentMessage): Result<Unit> {
        val response = chatApi.createMessage(message.toCreateMessageRequest())
        return if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message()))
        }
    }
}