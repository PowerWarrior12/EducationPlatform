package com.example.educationplatform.presentation.courseEdit.structure.lectureStageCreate

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
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.MainApplication
import com.example.educationplatform.R
import com.example.educationplatform.databinding.LectureStageCreateFragmentBinding
import com.example.educationplatform.domain.entities.Stage
import com.example.educationplatform.presentation.courseEdit.CourseEditViewModel
import com.example.educationplatform.presentation.courseEdit.structure.TextEditorMenu
import com.example.educationplatform.utils.extensions.launchWhenStarted
import com.example.educationplatform.utils.extensions.setWithProgressBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val PROCESS_MESSAGE = "PROCESS"
private const val ERROR_MESSAGE = "ERROR"

class LectureStageCreateFragment: Fragment(R.layout.lecture_stage_create_fragment) {
    @Inject
    lateinit var lectureCreateFactory: LectureStageCreateViewModel.LectureStageCreateViewModelFactory.Factory

    private val binding by viewBinding<LectureStageCreateFragmentBinding>()
    private val courseViewModel by navGraphViewModels<CourseEditViewModel>(R.id.edit_course_graph)
    private val lectureCreateViewModel by viewModels<LectureStageCreateViewModel> {
        lectureCreateFactory.create(args.moduleId, args.stageId)
    }
    private val args: LectureStageCreateFragmentArgs by navArgs()

    private val loadingSnack by lazy {
        Snackbar.make(binding.root, PROCESS_MESSAGE, Snackbar.LENGTH_INDEFINITE)
    }

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeModels()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initLectureCreateModel()
    }

    private fun initLectureCreateModel() {
        if (args.stageId >= 0) {
            val stage = courseViewModel.getStageById(args.stageId)
            binding.apply {
                editTitleText.setText(stage.title)
                editInfoText.setText(stage.info)
                textEditor.html = stage.data
            }
        }
    }

    private fun observeModels() {
        lectureCreateViewModel.newStageFlow
            .onEach { stage ->
                createNewStage(stage)
            }.launchWhenStarted(lifecycleScope)


        lectureCreateViewModel.updateStageFlow
            .onEach { stage ->
                updateStage(stage)
            }.launchWhenStarted(lifecycleScope)

        lectureCreateViewModel.errorFlow
            .onEach { error ->
                if (error) {
                    Toast.makeText(requireContext(), ERROR_MESSAGE, Toast.LENGTH_LONG).show()
                }
            }.launchWhenStarted(lifecycleScope)

        lectureCreateViewModel.processFlow
            .onEach { process ->
                when {
                    process -> loadingSnack.show()
                    else -> loadingSnack.dismiss()
                }
                setEnabledControllers(!process)
            }.launchWhenStarted(lifecycleScope)
    }

    private fun initViews() {
        TextEditorMenu(binding.menuEditor, binding.textEditor).init()
        binding.apply {
            saveButton.setOnClickListener {
                closeKeyBoard()
                save()
            }
            editTitleText.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    lectureCreateViewModel.updateTitle(p0.toString())
                }
                override fun afterTextChanged(p0: Editable?) {}
            })
            editInfoText.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    lectureCreateViewModel.updateInfo(p0.toString())
                }
                override fun afterTextChanged(p0: Editable?) {}
            })
        }
        loadingSnack.setWithProgressBar()
    }

    private fun closeKeyBoard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun setEnabledControllers(enable: Boolean) {
        binding.apply {
            titleText.isEnabled = enable
            saveButton.isEnabled = enable
            infoText.isEnabled = enable
        }
    }

    private fun createNewStage(stage: Stage) {
        courseViewModel.addModuleStageItem(stage)
        requireActivity().findNavController(R.id.container).popBackStack()
    }

    private fun updateStage(stage: Stage) {
        courseViewModel.updateModuleStageItems(stage)
        requireActivity().findNavController(R.id.container).popBackStack()
    }

    private fun save() {
        lectureCreateViewModel.updateData(binding.textEditor.html ?: "")
        lectureCreateViewModel.saveStage()
    }
}