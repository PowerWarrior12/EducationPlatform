package com.example.educationplatform.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.R
import com.example.educationplatform.databinding.PersonalInformationDialogFragmentBinding
import com.example.educationplatform.utils.TextWatcherImpl
import com.example.educationplatform.utils.extensions.launchWhenStarted
import kotlinx.coroutines.flow.onEach

class PersonalInformationDialog: DialogFragment(R.layout.personal_information_dialog_fragment) {
    private val binding by viewBinding<PersonalInformationDialogFragmentBinding>()
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
                    nameText.setText(user.name)
                    secondName.setText(user.secondName)
                    infoText.setText(user.info)
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
                profileViewModel.updatePersonalInformation()

            }
            nameText.addTextChangedListener(object: TextWatcherImpl() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!profileViewModel.setName(p0.toString())) {
                        nameText.error = errorValidationLabel
                    } else {
                        nameText.error = null
                    }
                }
            })
            secondName.addTextChangedListener(object: TextWatcherImpl() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!profileViewModel.setSecondName(p0.toString())) {
                        secondName.error = errorValidationLabel
                    } else {
                        secondName.error = null
                    }
                }
            })
            infoText.addTextChangedListener(object: TextWatcherImpl() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!profileViewModel.setInfo(p0.toString())) {
                        infoText.error = errorValidationLabel
                    } else {
                        infoText.error = null
                    }
                }
            })
        }
    }
}