package com.example.educationplatform.presentation.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.MainApplication
import com.example.educationplatform.MainGraphDirections
import com.example.educationplatform.R
import com.example.educationplatform.databinding.ProfileFragmentBinding
import com.example.educationplatform.utils.extensions.launchWhenStarted
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ProfileFragment : Fragment(R.layout.profile_fragment) {

    @Inject
    lateinit var profileViewModelFactory: ProfileViewModel.ProfileViewModelFactory

    private val binding by viewBinding<ProfileFragmentBinding>()
    private val profileViewModel by navGraphViewModels<ProfileViewModel>(R.id.menu_graph) {
        profileViewModelFactory
    }

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.apply {
            changePersonalInformation.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileToPersonalInformationDialog()
                requireActivity().findNavController(R.id.fragment_container).navigate(action)
            }
            changeEmail.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileToChangeEmailDialog()
                requireActivity().findNavController(R.id.fragment_container).navigate(action)
            }
            changePassword.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileToChangePasswordDialog()
                requireActivity().findNavController(R.id.fragment_container).navigate(action)
            }
            exitButton.setOnClickListener {
                val action = MainGraphDirections.actionGlobalAuthorizationFragment()
                requireActivity().findNavController(R.id.container).navigate(action)
            }
            sendChangeStatusRequest.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileToSendChangeStatusDialog()
                requireActivity().findNavController(R.id.fragment_container).navigate(action)
            }
        }
    }

    private fun observeModel() {
        profileViewModel.userFlow
            .onEach {
                binding.userFio.text = "${it.name} ${it.secondName}"
                changeStatusInterface(it.status)
            }.launchWhenStarted(lifecycleScope)
    }

    private fun changeStatusInterface(status: String) {
        val statusIsTeacher = status != "TEACHER"
        binding.apply {
            sendChangeStatusRequest.isVisible = statusIsTeacher
            changeStatusBorder.isVisible = statusIsTeacher
        }
    }
}