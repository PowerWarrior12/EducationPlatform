package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.Chat
import com.example.educationplatform.domain.repositories.ChatRepositoryRemote
import javax.inject.Inject

class LoadUserChatsInteractor @Inject constructor(
    private val chatRepositoryRemote: ChatRepositoryRemote
) {
    suspend operator fun invoke(): Result<List<Chat>> {
        return chatRepositoryRemote.getChatsByUser()
    }
}