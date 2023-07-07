package com.example.educationplatform.presentation.chat

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.MainApplication
import com.example.educationplatform.MainGraphDirections
import com.example.educationplatform.R
import com.example.educationplatform.databinding.ChatFragmentBinding
import com.example.educationplatform.presentation.chat.adapter.ChatsAdapter
import com.example.educationplatform.utils.extensions.launchWhenStarted
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ChatFragment: Fragment(R.layout.chat_fragment) {
    @Inject
    lateinit var chatViewModelFactory: ChatViewModel.ChatViewModelFactory

    private val chatViewModel by viewModels<ChatViewModel> {
        chatViewModelFactory
    }
    private val binding by viewBinding<ChatFragmentBinding>()
    private val chatsAdapter by lazy {
        ChatsAdapter(::onChatClick)
    }

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeModel()
    }

    private fun observeModel() {
        chatViewModel.chatsFlow
            .onEach { chats ->
                chatsAdapter.items = chats
            }.launchWhenStarted(lifecycleScope)

        chatViewModel.processFlow
            .onEach { isProcess ->
                binding.swipeLayout.isEnabled = !isProcess
            }.launchWhenStarted(lifecycleScope)
    }

    private fun initViews() {
        binding.apply {
            recyclerView.adapter = chatsAdapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            swipeLayout.setOnRefreshListener {
                chatViewModel.loadUserChats()
                binding.swipeLayout.isRefreshing = false
            }
        }
    }

    private fun onChatClick(chatId: Int) {
        val action = MainGraphDirections.actionGlobalMessengerFragment(chatId, chatViewModel.getChatTitle(chatId))
        requireActivity().findNavController(R.id.container).navigate(action)
    }
}