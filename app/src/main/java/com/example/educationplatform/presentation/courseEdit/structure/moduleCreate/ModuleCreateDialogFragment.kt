package com.example.educationplatform.presentation.courseEdit.structure.moduleCreate

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.MainApplication
import com.example.educationplatform.R
import com.example.educationplatform.databinding.ModuleCreateDialogBinding
import com.example.educationplatform.domain.entities.Module
import com.example.educationplatform.presentation.courseEdit.CourseEditViewModel
import com.example.educationplatform.utils.extensions.launchWhenStarted
import com.example.educationplatform.utils.extensions.setWithProgressBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val PROCESS_MESSAGE = "PROCESS"
private const val ERROR_MESSAGE = "ERROR"

class ModuleCreateDialogFragment: DialogFragment(R.layout.module_create_dialog) {

    @Inject
    lateinit var moduleCreateViewModelFactory: ModuleCreateViewModel.ModuleCreateViewModelFactory.Factory

    private val binding by viewBinding<ModuleCreateDialogBinding>()
    private val args: ModuleCreateDialogFragmentArgs by navArgs()
    private val courseEditViewModel: CourseEditViewModel by navGraphViewModels(R.id.edit_course_graph)
    private val moduleCreateViewModel by viewModels<ModuleCreateViewModel> {
        moduleCreateViewModelFactory.create(args.courseId, args.moduleId)
    }
    private val loadingSnack by lazy {
        Snackbar.make(binding.root, PROCESS_MESSAGE, Snackbar.LENGTH_INDEFINITE)
    }
    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE,
            android.R.style.ThemeOverlay_Material_Dialog_Alert)
        observeModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initModels()
    }

    private fun observeModel() {
        moduleCreateViewModel.createModuleFlow
            .onEach { module ->
                createModule(module)
            }.launchWhenStarted(lifecycleScope)

        moduleCreateViewModel.updateModuleFlow
            .onEach { module ->
                updateModule(module)
            }.launchWhenStarted(lifecycleScope)

        moduleCreateViewModel.errorFlow
            .onEach { error ->
                if (error)
                    Toast.makeText(requireContext(), ERROR_MESSAGE, Toast.LENGTH_LONG).show()
            }.launchWhenStarted(lifecycleScope)

        moduleCreateViewModel.processFlow
            .onEach { process ->
                when {
                    process -> loadingSnack.show()
                    else -> loadingSnack.dismiss()
                }
                setEnabledControllers(!process)
            }.launchWhenStarted(lifecycleScope)
    }

    private fun initModels() {
        if (args.moduleId >= 0) {
            val module = courseEditViewModel.getModuleById(args.moduleId)
            binding.apply {
                editTitleText.setText(module.title)
                editInfoText.setText(module.info)
                moduleCreateViewModel.updateScore(module.score)
            }
        }
    }

    private fun initViews() {
        binding.apply {
            editTitleText.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    moduleCreateViewModel.updateTitle(p0.toString())
                }
                override fun afterTextChanged(p0: Editable?) {}
            })
            editInfoText.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    moduleCreateViewModel.updateInfo(p0.toString())
                }
                override fun afterTextChanged(p0: Editable?) {}
            })
            saveButton.setOnClickListener {
                closeKeyBoard()
                moduleCreateViewModel.save()
            }

            loadingSnack.setWithProgressBar()
        }
    }

    private fun setEnabledControllers(enable: Boolean) {
        binding.apply {
            saveButton.isEnabled = enable
            titleText.isEnabled = enable
            infoText.isEnabled = enable
        }
    }

    private fun closeKeyBoard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun createModule(module: Module) {
        courseEditViewModel.addCourseModuleItem(module)
        requireActivity().findNavController(R.id.container).popBackStack()
    }

    private fun updateModule(module: Module) {
        courseEditViewModel.updateCourseModuleItem(module)
        requireActivity().findNavController(R.id.container).popBackStack()
    }
}