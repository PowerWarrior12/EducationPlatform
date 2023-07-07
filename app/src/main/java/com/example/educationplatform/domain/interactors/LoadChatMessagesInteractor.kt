package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.Message
import com.example.educationplatform.domain.repositories.ChatRepositoryRemote
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadChatMessagesInteractor @Inject constructor(
    private val chatRepositoryRemote: ChatRepositoryRemote
) {
    suspend operator fun invoke(chatId: Int): Flow<List<Message>> {
        return chatRepositoryRemote.getMessagesByChat(chatId)
    }
}