package com.example.educationplatform.presentation.cource.module.lecture

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.R
import com.example.educationplatform.databinding.LectureStudyFragmentBinding
import com.example.educationplatform.presentation.cource.module.ModulesViewModel
import com.example.educationplatform.utils.extensions.launchWhenStarted
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

const val STAGE_INDEX = "stageIndex"

class LectureDisplayFragment: Fragment(R.layout.lecture_study_fragment) {

    private val binding by viewBinding<LectureStudyFragmentBinding>()
    private val modulesViewModel by navGraphViewModels<ModulesViewModel>(R.id.course_graph)
    private val stageViewModel by viewModels<LectureDisplayViewModel>()

    private var observeJob: Job? = null
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
            editor.setInputEnabled(false)
            nextButton.setOnClickListener {
                arguments?.getInt(STAGE_INDEX)?.let { stageIndex ->
                    modulesViewModel.goToNextStageOrModule(stageIndex)
                }
            }
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

        stageViewModel.titleFlow
            .onEach { title ->
                binding.titleText.text = title
            }.launchWhenStarted(lifecycleScope)


        stageViewModel.infoFlow
            .onEach { info ->
                binding.infoText.text = info
            }.launchWhenStarted(lifecycleScope)


        stageViewModel.lectureFlow
            .onEach { lectureData ->
                binding.editor.html = lectureData
            }.launchWhenStarted(lifecycleScope)
    }

    companion object {
        fun newInstance(stageIndex: Int): LectureDisplayFragment {
            return LectureDisplayFragment().apply {
                arguments = bundleOf(STAGE_INDEX to stageIndex)
            }
        }
    }
}