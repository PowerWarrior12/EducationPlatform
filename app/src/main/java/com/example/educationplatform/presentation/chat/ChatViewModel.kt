package com.example.educationplatform.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.domain.entities.Chat
import com.example.educationplatform.domain.interactors.LoadCurrentUserInteractor
import com.example.educationplatform.domain.interactors.LoadUserChatsInteractor
import com.example.educationplatform.presentation.chat.adapter.ChatItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatViewModel(
    private val loadUserChatsInteractor: LoadUserChatsInteractor
): ViewModel() {
    private val chats: MutableList<Chat> = mutableListOf()

    private val _chatsFlow = MutableSharedFlow<List<ChatItem>>(1, 1, BufferOverflow.DROP_OLDEST)
    private val _errorFlow = MutableStateFlow<Boolean>(false)
    private val _processFlow = MutableStateFlow<Boolean>(false)

    val chatsFlow = _chatsFlow.asSharedFlow()
    val errorFlow = _errorFlow.asStateFlow()
    val processFlow = _processFlow.asStateFlow()

    init {
        loadUserChats()
    }

    fun getChatTitle(chatId: Int): String {
        return chats.firstOrNull{ chat ->
            chat.id == chatId
        }?.title ?: ""
    }
    fun loadUserChats() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _processFlow.emit(true)
                loadUserChatsInteractor().onSuccess { chats ->
                    this@ChatViewModel.chats.apply {
                        clear()
                        addAll(chats)
                    }
                    _chatsFlow.emit(chats.map<Chat, ChatItem> { chat ->
                        ChatItem.UserChat(chat)
                    })
                    cancel()
                }.onFailure {
                    _errorFlow.emit(true)
                }
                _processFlow.emit(false)
            }
        }
    }

    class ChatViewModelFactory @Inject constructor(
        private val loadUserChatsInteractor: LoadUserChatsInteractor,
        private val loadCurrentUserInteractor: LoadCurrentUserInteractor
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChatViewModel(loadUserChatsInteractor) as T
        }
    }
}