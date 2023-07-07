package com.example.educationplatform.presentation.chat.messenger

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.MainApplication
import com.example.educationplatform.R
import com.example.educationplatform.databinding.MessengerFragmentBinding
import com.example.educationplatform.presentation.chat.messenger.adapter.MessagesAdapter
import com.example.educationplatform.utils.extensions.launchWhenStarted
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MessengerFragment : Fragment(R.layout.messenger_fragment) {
    @Inject
    lateinit var messengerViewModelFactory: MessengerViewModel.MessengerViewModelFactory.Factory

    private val args by navArgs<MessengerFragmentArgs>()
    private val binding by viewBinding<MessengerFragmentBinding>()
    private val messengerViewModel by viewModels<MessengerViewModel> {
        messengerViewModelFactory.create(args.chatId, args.chatTitle)
    }
    private val messagesAdapter by lazy {
        MessagesAdapter()
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
        messengerViewModel.chatTitleFlow
            .onEach { title ->
                binding.titleText.text = title
            }.launchWhenStarted(lifecycleScope)

        messengerViewModel.messagesFlow
            .onEach { messageItems ->
                messagesAdapter.items = messageItems
            }.launchWhenStarted(lifecycleScope)
    }

    private fun initViews() {
        binding.apply {
            recyclerView.adapter = messagesAdapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            sendButton.setOnClickListener {
                messengerViewModel.sendMessage(sendMessageText.text.toString())
            }
        }
    }
}