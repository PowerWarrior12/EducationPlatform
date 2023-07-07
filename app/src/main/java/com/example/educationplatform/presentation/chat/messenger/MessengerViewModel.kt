package com.example.educationplatform.presentation.chat.messenger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.domain.entities.SentMessage
import com.example.educationplatform.domain.interactors.LoadChatMessagesInteractor
import com.example.educationplatform.domain.interactors.SendMessageInteractor
import com.example.educationplatform.presentation.chat.messenger.adapter.MessageItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessengerViewModel(
    private val chatId: Int,
    private val chatTitle: String,
    private val loadChatMessagesInteractor: LoadChatMessagesInteractor,
    private val sendMessageInteractor: SendMessageInteractor
) : ViewModel() {

    private val _chatTitleFlow = MutableStateFlow(chatTitle)
    private val _messagesFlow =
        MutableSharedFlow<List<MessageItem>>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _errorFlow = MutableStateFlow(false)
    private val _processFlow = MutableStateFlow(false)

    val chatTitleFlow = _chatTitleFlow.asStateFlow()
    val messagesFlow = _messagesFlow.asSharedFlow()
    val errorFlow = _errorFlow.asStateFlow()
    val processFlow = _processFlow.asStateFlow()

    init {
        loadMessages()
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _processFlow.emit(true)

                sendMessageInteractor(SentMessage(chatId, text)).onFailure {
                    _errorFlow.emit(true)
                }
                _processFlow.emit(false)
            }
        }
    }

    private fun loadMessages() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loadChatMessagesInteractor(chatId)
                    .collect { messages ->
                        _messagesFlow.emit(messages.map { message ->
                            if (message.userName == "") {
                                MessageItem.CurrentUserMessageItem(message)
                            } else {
                                MessageItem.UserMessageItem(message)
                            }
                        })
                    }
            }
        }
    }

    class MessengerViewModelFactory @AssistedInject constructor(
        @Assisted("chatId") private val chatId: Int,
        @Assisted private val chatTitle: String,
        private val loadChatMessagesInteractor: LoadChatMessagesInteractor,
        private val sendMessageInteractor: SendMessageInteractor
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MessengerViewModel(
                chatId, chatTitle, loadChatMessagesInteractor, sendMessageInteractor
            ) as T
        }

        @AssistedFactory
        interface Factory {
            fun create(
                @Assisted("chatId") chatId: Int,
                @Assisted chatTitle: String
            ): MessengerViewModelFactory
        }
    }
}