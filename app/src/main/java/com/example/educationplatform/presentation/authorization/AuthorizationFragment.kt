package com.example.educationplatform.presentation.authorization

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.MainApplication
import com.example.educationplatform.R
import com.example.educationplatform.databinding.AuthFragmentBinding
import com.google.android.material.textfield.TextInputEditText
import javax.inject.Inject

class AuthorizationFragment: Fragment(R.layout.auth_fragment) {

    @Inject
    lateinit var viewModelFactory: AuthorizationViewModel.AuthorizationViewModelFactory

    val authorizationViewModel: AuthorizationViewModel by viewModels { viewModelFactory }
    val binding: AuthFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.appComponent.inject(this)
        super.onAttach(context)
    }

    private fun initViews() {
        binding.loginButton.setOnClickListener {
            authorizationViewModel.authorization()
        }

        binding.registrationButton.setOnClickListener {

        }

        binding.editLoginText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (authorizationViewModel.updateLogin(p0.toString())) {
                    removeEditTextError(binding.editLoginText)
                } else {
                    setLoginValidationError()
                }
            }

        })

        binding.editPasswordText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (authorizationViewModel.updatePassword(p0.toString())) {
                    removeEditTextError(binding.editPasswordText)
                } else {
                    setPasswordValidationError()
                }
            }

        })
    }

    private fun setLoginValidationError() {
        binding.editLoginText.setError("Error validation")
    }

    private fun setPasswordValidationError() {
        binding.editPasswordText.setError("Error validation")
    }

    private fun removeEditTextError(editText: TextInputEditText) {
        editText.setError(null)
    }

    private fun setObservation() {

    }
    companion object {
        fun newInstance(): AuthorizationFragment {
            return AuthorizationFragment()
        }
    }
}