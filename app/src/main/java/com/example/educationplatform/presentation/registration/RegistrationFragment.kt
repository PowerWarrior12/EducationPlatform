package com.example.educationplatform.presentation.registration

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.MainApplication
import com.example.educationplatform.R
import com.example.educationplatform.databinding.RegistrationFragmentBinding
import com.example.educationplatform.utils.TextWatcherImpl
import com.example.educationplatform.utils.extensions.launchWhenStarted
import com.example.educationplatform.utils.extensions.setWithProgressBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val REGISTRATION_MESSAGE = "REGISTRATION PROCESS"
private const val ERROR_MESSAGE = "REGISTRATION ERROR"

class RegistrationFragment: Fragment(R.layout.registration_fragment) {

    @Inject
    lateinit var viewModelFactory: RegistrationViewModel.RegistrationViewModelFactory

    private val binding by viewBinding<RegistrationFragmentBinding>()
    private val viewModel by viewModels<RegistrationViewModel> {
        viewModelFactory
    }
    private val errorValidationLabel by lazy {
        resources.getString(R.string.error_validation_label)
    }

    private val loadingSnack by lazy {
        Snackbar.make(binding.root, REGISTRATION_MESSAGE, Snackbar.LENGTH_INDEFINITE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeModel()
    }

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.apply {
            nameText.addTextChangedListener(object: TextWatcherImpl() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!viewModel.updateName(p0!!.toString())) {
                        nameText.error = errorValidationLabel
                    } else {
                        nameText.error = null
                    }
                }
            })
            secondName.addTextChangedListener(object: TextWatcherImpl() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!viewModel.updateSecondName(p0!!.toString())) {
                        secondName.error = errorValidationLabel
                    } else {
                        secondName.error = null
                    }
                }
            })
            loginText.addTextChangedListener(object: TextWatcherImpl() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!viewModel.updateLogin(p0!!.toString())) {
                        loginText.error = errorValidationLabel
                    } else {
                        loginText.error = null
                    }
                }
            })
            passwordText.addTextChangedListener(object: TextWatcherImpl() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!viewModel.updatePassword(p0!!.toString())) {
                        passwordText.error = errorValidationLabel
                    } else {
                        passwordText.error = null
                    }
                }
            })
            replyPasswordText.addTextChangedListener(object: TextWatcherImpl() {
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!viewModel.updateReplyPassword(p0!!.toString())) {
                        replyPasswordText.error = errorValidationLabel
                    } else {
                        replyPasswordText.error = null
                    }
                }
            })
            confirmButton.setOnClickListener {
                closeKeyBoard()
                viewModel.registration()
            }
            toolbar.actionButton.setOnClickListener {
                requireActivity().findNavController(R.id.container).popBackStack()
            }
            binding.root.setOnClickListener {
                closeKeyBoard()
            }
            loadingSnack.setWithProgressBar()
        }
    }

    private fun observeModel() {
        viewModel.onRegistrationFlow
            .onEach {
                requireActivity().findNavController(R.id.container).popBackStack()
            }.launchWhenStarted(lifecycleScope)

        viewModel.errorFlow
            .onEach { isError ->
                if (isError)
                    Toast.makeText(requireContext(), ERROR_MESSAGE, Toast.LENGTH_LONG).show()
            }.launchWhenStarted(lifecycleScope)

        viewModel.processFlow
            .onEach {
                if (it) {
                    loadingSnack.show()
                } else {
                    loadingSnack.dismiss()
                }
                setControllersEnable(!it)
            }.launchWhenStarted(lifecycleScope)
    }

    private fun closeKeyBoard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun setControllersEnable(enable: Boolean) {
        binding.confirmButton.isEnabled = enable
        binding.toolbar.actionButton.isEnabled = enable
    }

    companion object {
        fun newInstance(): RegistrationFragment {
            return RegistrationFragment()
        }
    }
}