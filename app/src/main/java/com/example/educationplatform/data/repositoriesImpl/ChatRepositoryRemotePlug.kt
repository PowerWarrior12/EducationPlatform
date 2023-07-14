package com.example.educationplatform.data.repositoriesImpl

import com.example.educationplatform.domain.entities.Chat
import com.example.educationplatform.domain.entities.Message
import com.example.educationplatform.domain.entities.SentMessage
import com.example.educationplatform.domain.repositories.ChatRepositoryRemote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class ChatRepositoryRemotePlug @Inject constructor(): ChatRepositoryRemote {

    private val messengerFlow: MutableStateFlow<MutableList<Message>> = MutableStateFlow (
        mutableListOf(
        )
    )

    override suspend fun getChatsByUser(): Result<List<Chat>> {
        return Result.success(
            listOf(
            )
        )
    }

    override suspend fun getMessagesByChat(chatId: Int): Flow<List<Message>> {
        val messengerFlow: MutableStateFlow<MutableList<Message>> = MutableStateFlow (
            mutableListOf(
                Message( "https://ibb.co/j3TmXGc", "Maria", "Как мне делать это задание")
            )
        )
        CoroutineScope(coroutineContext)
            .launch {
                for (i in 0..100) {
                    delay(5000)
                    val newMessages = messengerFlow.value.toMutableList()
                    newMessages.add(Message( "https://ibb.co/j3TmXGc", "Maria", "Ну чё народ"))
                    messengerFlow.value = newMessages
                }
            }
        return messengerFlow.map { it.toList() }
    }

    override suspend fun createMessage(message: SentMessage): Result<Unit> {
        val newMessages = messengerFlow.value.toMutableList()
        newMessages.add(Message( "https://i.ibb.co/r7svSCs/python.jpg", "Peter@Uhlimov", message.text))

        messengerFlow.value = newMessages
        return Result.success(Unit)
    }
}
