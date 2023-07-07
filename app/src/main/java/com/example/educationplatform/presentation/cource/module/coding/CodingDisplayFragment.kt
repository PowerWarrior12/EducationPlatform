package com.example.educationplatform.presentation.cource.module.coding

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.MainApplication
import com.example.educationplatform.R
import com.example.educationplatform.databinding.CodingStudyFragmentBinding
import com.example.educationplatform.domain.entities.CheckResult
import com.example.educationplatform.presentation.cource.CourseViewModel
import com.example.educationplatform.presentation.cource.module.ModulesViewModel
import com.example.educationplatform.presentation.cource.module.lecture.STAGE_INDEX
import com.example.educationplatform.presentation.languagesMap
import com.example.educationplatform.utils.extensions.launchWhenStarted
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class CodingDisplayFragment : Fragment(R.layout.coding_study_fragment) {

    @Inject
    lateinit var stageViewModelFactory: CodingDisplayViewModel.CodingDisplayViewModelFactory

    private val binding by viewBinding<CodingStudyFragmentBinding>()
    private val modulesViewModel by navGraphViewModels<ModulesViewModel>(R.id.course_graph)
    private val courseViewModel by navGraphViewModels<CourseViewModel>(R.id.course_graph)
    private val stageViewModel by viewModels<CodingDisplayViewModel> {
        stageViewModelFactory
    }
    private var testsOpen = false
    private var observeJob: Job? = null

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeModuleModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeJob = lifecycleScope.launch {
            observeStageModel()
        }
    }

    override fun onStop() {
        super.onStop()
        observeJob?.cancel()
    }

    private fun initViews() {
        binding.apply {
            textEditor.setInputEnabled(false)
            nextButton.setOnClickListener {
                arguments?.getInt(STAGE_INDEX)?.let { stageIndex ->
                    modulesViewModel.goToNextStageOrModule(stageIndex)
                }
            }
            testsButton.root.setOnClickListener {
                showOrCollapseTestData()
            }
            checkButton.setOnClickListener {
                stageViewModel.checkCode(binding.codeEditorLayout.text)
            }
            codeEditorLayout.showMinimap = false
        }
    }

    private fun observeModuleModel() {
        modulesViewModel.currentModuleFlow
            .onEach { takesModuleDisplay ->
                arguments?.getInt(STAGE_INDEX)?.let { stageIndex ->
                    stageViewModel.setStage(takesModuleDisplay.stages[stageIndex])
                }
            }.launchWhenStarted(lifecycleScope)
    }

    private fun observeStageModel() {

        stageViewModel.processFlow
            .onEach { process ->
                binding.processLayout.isVisible = process
            }.launchWhenStarted(lifecycleScope)

        stageViewModel.titleFlow
            .onEach { title ->
                binding.titleText.text = title
            }.launchWhenStarted(lifecycleScope)

        stageViewModel.infoFlow
            .onEach { info ->
                binding.infoText.text = info
            }.launchWhenStarted(lifecycleScope)


        stageViewModel.languageFlow
            .onEach { language ->
                binding.languageText.text = language
                binding.codeEditorLayout.languageRuleBook = languagesMap[language]
            }.launchWhenStarted(lifecycleScope)

        stageViewModel.taskFlow
            .onEach { task ->
                binding.textEditor.html = task
            }.launchWhenStarted(lifecycleScope)

        stageViewModel.testDataFlow
            .onEach { testDataList ->
                testDataList.firstOrNull()?.let { testData ->
                    binding.inputData.text = testData.inputData
                    binding.outputData.text = testData.outputData
                }
            }.launchWhenStarted(lifecycleScope)

        stageViewModel.codeDataFlow
            .onEach { code ->
                binding.codeEditorLayout.text = code
            }.launchWhenStarted(lifecycleScope)

        stageViewModel.codeCheckFlow
            .onEach(::processCheckCodeResult)
            .launchWhenStarted(lifecycleScope)

        stageViewModel.updateStage
            .onEach { takesStage ->
                modulesViewModel.updateTakesStage(takesStage)
                courseViewModel.updateStage(takesStage)
            }
            .launchWhenStarted(lifecycleScope)
    }

    private fun showOrCollapseTestData() {
        binding.apply {
            testsOpen = !testsOpen
            testsButton.arrowImage.setImageResource(
                if (testsOpen) {
                    R.drawable.bottom_arrow
                } else {
                    R.drawable.right_arrow
                }
            )

            if (testLayout.isVisible) {
                testLayout.visibility = View.GONE
            } else {
                testLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun processCheckCodeResult(checkCodeResult: CheckResult) {
        if (checkCodeResult.status) {
            binding.nextButton.visibility = View.VISIBLE
        }
        checkCodeResult.message?.let { message ->
            binding.consoleText.text = message
            binding.consoleText.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance(stageIndex: Int): CodingDisplayFragment {
            return CodingDisplayFragment().apply {
                arguments = bundleOf(STAGE_INDEX to stageIndex)
            }
        }
    }
}