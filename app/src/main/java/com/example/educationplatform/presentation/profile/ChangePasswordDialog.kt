package com.example.educationplatform.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.R
import com.example.educationplatform.databinding.ChangePasswordDialogFragmentBinding
import com.example.educationplatform.utils.TextWatcherImpl
import com.example.educationplatform.utils.extensions.launchWhenStarted
import kotlinx.coroutines.flow.onEach

class ChangePasswordDialog : DialogFragment(R.layout.change_password_dialog_fragment) {
    private val binding by viewBinding<ChangePasswordDialogFragmentBinding>()
    private val profileViewModel by navGraphViewModels<ProfileViewModel>(R.id.menu_graph)

    private val errorValidationLabel by lazy {
        resources.getString(R.string.error_validation_label)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            DialogFragment.STYLE_NO_TITLE,
            android.R.style.ThemeOverlay_Material_Dialog_Alert
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeModel()
    }

    private fun observeModel() {
        profileViewModel.userFlow
            .onEach { user ->
                binding.apply {
                    currentPasswordText.setText(user.password)
                }
            }.launchWhenStarted(lifecycleScope)

        profileViewModel.userUpdatedFlow
            .onEach {
                requireActivity().findNavController(R.id.fragment_container).popBackStack()
            }.launchWhenStarted(lifecycleScope)

    }

    private fun initViews() {
        binding.apply {
            confirmButton.setOnClickListener {
                profileViewModel.updatePassword()
            }
            currentPasswordText.addTextChangedListener(object: TextWatcherImpl() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!profileViewModel.setCurrentPassword(p0.toString())) {
                        currentPasswordText.error = errorValidationLabel
                    } else {
                        currentPasswordText.error = null
                    }
                }
            })

            newPasswordText.addTextChangedListener(object: TextWatcherImpl() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!profileViewModel.setNewPassword(p0.toString())) {
                        newPasswordText.error = errorValidationLabel
                    } else {
                        newPasswordText.error = null
                    }
                }
            })
        }
    }
}