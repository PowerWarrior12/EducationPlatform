package com.example.educationplatform.domain.interactors

import com.example.educationplatform.domain.entities.SentMessage
import com.example.educationplatform.domain.repositories.ChatRepositoryRemote
import javax.inject.Inject

class SendMessageInteractor @Inject constructor(
    private val chatRepositoryRemote: ChatRepositoryRemote
) {
    suspend operator fun invoke(message: SentMessage): Result<Unit> {
        return chatRepositoryRemote.createMessage(message)
    }
}