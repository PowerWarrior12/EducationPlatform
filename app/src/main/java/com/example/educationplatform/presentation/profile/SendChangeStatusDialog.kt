package com.example.educationplatform.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.R
import com.example.educationplatform.databinding.SendChangeStatusDialogBinding
import com.example.educationplatform.utils.extensions.launchWhenStarted
import kotlinx.coroutines.flow.onEach

class SendChangeStatusDialog : DialogFragment(R.layout.send_change_status_dialog) {

    private val binding by viewBinding<SendChangeStatusDialogBinding>()
    private val profileViewModel by navGraphViewModels<ProfileViewModel>(R.id.menu_graph)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NO_TITLE,
            android.R.style.ThemeOverlay_Material_Dialog_Alert
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeModel()
    }

    private fun observeModel() {
        profileViewModel.requestToSendStatusFlow.onEach {
            dismiss()
        }.launchWhenStarted(lifecycleScope)
    }

    private fun initViews() {
        binding.apply {
            sendButton.setOnClickListener {
                profileViewModel.sendChangeStatusRequest(messageText.text.toString())
            }
        }
    }
}