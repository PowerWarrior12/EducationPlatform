package com.example.educationplatform.presentation.cource.module.blockDiagram

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.MainApplication
import com.example.educationplatform.R
import com.example.educationplatform.databinding.BlockDiagramStudyFragmentBinding
import com.example.educationplatform.domain.entities.CheckResult
import com.example.educationplatform.presentation.cource.CourseViewModel
import com.example.educationplatform.presentation.cource.module.ModuleFragmentDirections
import com.example.educationplatform.presentation.cource.module.ModulesViewModel
import com.example.educationplatform.presentation.cource.module.lecture.STAGE_INDEX
import com.example.educationplatform.utils.extensions.launchWhenStarted
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

const val DEFAULT_STAGE_INDEX = 0

class BlockDiagramDisplayFragment : Fragment(R.layout.block_diagram_study_fragment) {
    @Inject
    lateinit var stageViewModelFactory: BlockDiagramDisplayViewModel.BlockDiagramDisplayViewModelFactory

    private val binding by viewBinding<BlockDiagramStudyFragmentBinding>()
    private val modulesViewModel by navGraphViewModels<ModulesViewModel>(R.id.course_graph)
    private val courseViewModel by navGraphViewModels<CourseViewModel>(R.id.course_graph)
    private val stageViewModel by viewModels<BlockDiagramDisplayViewModel> {
        stageViewModelFactory
    }
    private var observeJob: Job? = null

    private var blockDiagramDisplayKey = "BlockDiagramEditKey"
    private var testsOpen: Boolean = false

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeModuleModel()
        blockDiagramDisplayKey = "$blockDiagramDisplayKey${arguments?.getInt(STAGE_INDEX)?:DEFAULT_STAGE_INDEX}"
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

    private fun observeModuleModel() {
        modulesViewModel.currentModuleFlow
            .onEach { takesModuleDisplay ->
                arguments?.getInt(STAGE_INDEX)?.let { stageIndex ->
                    stageViewModel.setStage(takesModuleDisplay.stages[stageIndex])
                }
            }.launchWhenStarted(lifecycleScope)
    }

    private fun observeStageModel() {

        stageViewModel.titleFlow
            .onEach { binding.titleText.text = it }
            .launchWhenStarted(lifecycleScope)

        stageViewModel.infoFlow
            .onEach { binding.infoText.text = it }
            .launchWhenStarted(lifecycleScope)

        stageViewModel.taskFlow
            .onEach { task ->
                binding.textEditor.html = task
            }.launchWhenStarted(lifecycleScope)

        stageViewModel.processFlow
            .onEach { process ->
                binding.processLayout.isVisible = process
            }.launchWhenStarted(lifecycleScope)

        stageViewModel.testDataFlow
            .onEach { testDataList ->
                testDataList.firstOrNull()?.let { testData ->
                    binding.inputData.text = testData.inputData
                    binding.outputData.text = testData.outputData
                }
            }.launchWhenStarted(lifecycleScope)

        stageViewModel.blockDataFlow
            .onEach { blockData ->
                binding.blockDiagram.loadConfigurations(blockData)
            }.launchWhenStarted(lifecycleScope)

        stageViewModel.checkFlow
            .onEach (::processCheckResult)
            .launchWhenStarted(lifecycleScope)

        stageViewModel.updateStage
            .onEach { takesStage ->
                courseViewModel.updateStage(takesStage)
                modulesViewModel.updateTakesStage(takesStage)
            }
            .launchWhenStarted(lifecycleScope)
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
            blockDiagram.setInteraction(false)
            blockDiagramLayout.setOnClickListener {
                val data = binding.blockDiagram.saveConfigurations()
                val action =
                    ModuleFragmentDirections.actionModuleFragmentToBlockDiagramEditFragment(data, blockDiagramDisplayKey)
                requireActivity().findNavController(R.id.container).navigate(action)
            }

            checkButton.setOnClickListener {
                stageViewModel.checkBlockDiagram(blockDiagram)
            }

            requireActivity().findNavController(R.id.container).currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
                blockDiagramDisplayKey
            )?.observe(viewLifecycleOwner) { blockData ->
                stageViewModel.updateBlockData(blockData)
            }
        }
    }

    private fun processCheckResult(checkResult: CheckResult) {
        if (checkResult.status) {
            binding.nextButton.visibility = View.VISIBLE
        }
        checkResult.message?.let { message ->
            binding.consoleText.text = message
            binding.consoleText.visibility = View.VISIBLE
        }
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

    private fun updateStage() {

    }

    companion object {
        fun newInstance(stageIndex: Int): BlockDiagramDisplayFragment {
            return BlockDiagramDisplayFragment().apply {
                arguments = bundleOf(STAGE_INDEX to stageIndex)
            }
        }
    }
}