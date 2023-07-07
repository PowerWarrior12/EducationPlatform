package com.example.educationplatform.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.R
import com.example.educationplatform.databinding.ChangeEmailDialogFragmentBinding
import com.example.educationplatform.utils.TextWatcherImpl
import com.example.educationplatform.utils.extensions.launchWhenStarted
import kotlinx.coroutines.flow.onEach

class ChangeEmailDialog: DialogFragment(R.layout.change_email_dialog_fragment) {

    private val binding by viewBinding<ChangeEmailDialogFragmentBinding>()
    private val profileViewModel by navGraphViewModels<ProfileViewModel>(R.id.menu_graph)

    private val errorValidationLabel by lazy {
        resources.getString(R.string.error_validation_label)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            DialogFragment.STYLE_NO_TITLE,
            android.R.style.ThemeOverlay_Material_Dialog_Alert
        );
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
                    emailText.setText(user.email)
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
                profileViewModel.updateEmail()
            }

            emailText.addTextChangedListener(object: TextWatcherImpl() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!profileViewModel.setEmail(p0.toString())) {
                        emailText.error = errorValidationLabel
                    } else {
                        emailText.error = null
                    }
                }
            })
        }
    }
}