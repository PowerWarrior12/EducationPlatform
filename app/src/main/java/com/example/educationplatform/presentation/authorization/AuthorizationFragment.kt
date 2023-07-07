package com.example.educationplatform.presentation.authorization

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.MainApplication
import com.example.educationplatform.MainGraphDirections
import com.example.educationplatform.R
import com.example.educationplatform.databinding.AuthFragmentBinding
import com.example.educationplatform.utils.extensions.launchWhenStarted
import com.example.educationplatform.utils.extensions.setWithProgressBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


private const val AUTHORIZATION_MESSAGE = "AUTHORIZATION PROCESS"
private const val ERROR_MESSAGE = "AUTHORIZATION ERROR"

class AuthorizationFragment : Fragment(R.layout.auth_fragment) {

    @Inject
    lateinit var viewModelFactory: AuthorizationViewModel.AuthorizationViewModelFactory

    private val authorizationViewModel: AuthorizationViewModel by viewModels { viewModelFactory }
    private val binding: AuthFragmentBinding by viewBinding()
    private val loadingSnack by lazy {
        Snackbar.make(binding.root, AUTHORIZATION_MESSAGE, Snackbar.LENGTH_INDEFINITE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeModel()
    }

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.appComponent.inject(this)
        super.onAttach(context)
    }

    private fun observeModel() {
        authorizationViewModel.authorizationFlow
            .onEach {
                val action = MainGraphDirections.actionGlobalBottomMenuFragment()
                binding.root.findNavController().navigate(action)
            }.launchWhenStarted(lifecycleScope)

        authorizationViewModel.errorFlow
            .onEach {
                if (it)
                    Toast.makeText(requireContext(), ERROR_MESSAGE, Toast.LENGTH_LONG).show()
            }.launchWhenStarted(lifecycleScope)

        authorizationViewModel.processFlow
            .onEach {
                if (it) {
                    loadingSnack.show()
                } else {
                    loadingSnack.dismiss()
                }
                setEnabledControllers(!it)
            }.launchWhenStarted(lifecycleScope)
    }

    private fun setEnabledControllers(enable: Boolean) {
        binding.apply {
            loginButton.isEnabled = enable
            registrationButton.isEnabled = enable
        }
    }

    private fun initViews() {
        binding.loginButton.setOnClickListener {
            authorizationViewModel.authorization()
            closeKeyBoard()
        }

        binding.registrationButton.setOnClickListener {
            closeKeyBoard()
            val action =
                AuthorizationFragmentDirections.actionAuthorizationFragmentToRegistrationFragment()
            it.findNavController().navigate(action)
        }

        binding.editLoginText.addTextChangedListener(object : TextWatcher {
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

        binding.editPasswordText.addTextChangedListener(object : TextWatcher {
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

        binding.apply {
            root.setOnClickListener {
                closeKeyBoard()
            }
        }

        loadingSnack.setWithProgressBar()
    }

    private fun closeKeyBoard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun setLoginValidationError() {
        binding.editLoginText.setError("Error validation")
    }

    private fun setPasswordValidationError() {
        binding.editPasswordText.setError("Error validation")
    }

    private fun removeEditTextError(editText: TextInputEditText) {
        editText.error = null
    }

    companion object {
        fun newInstance(): AuthorizationFragment {
            return AuthorizationFragment()
        }
    }
}