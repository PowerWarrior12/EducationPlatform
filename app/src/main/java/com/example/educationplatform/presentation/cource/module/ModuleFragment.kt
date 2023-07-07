package com.example.educationplatform.presentation.cource.module

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.R
import com.example.educationplatform.databinding.ModuleFragmentBinding
import com.example.educationplatform.domain.entities.TakesStage
import com.example.educationplatform.presentation.courseEdit.structure.StageTypes
import com.example.educationplatform.utils.extensions.launchWhenStarted
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.onEach

private const val ERROR_GO_TO_NEXT_MODULE_MESSAGE = "You have not completed all the steps in this module"

class ModuleFragment: Fragment(R.layout.module_fragment) {

    private val args by navArgs<ModuleFragmentArgs>()
    private val binding by viewBinding<ModuleFragmentBinding>()
    private val viewModel by navGraphViewModels<ModulesViewModel>(R.id.course_graph)
    private var stagesAdapter: StagesAdapter? = null

    private val loadingSnack by lazy {
        Snackbar.make(binding.root, ERROR_GO_TO_NEXT_MODULE_MESSAGE, Snackbar.LENGTH_LONG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeModel()
    }

    private fun prepareModel() {
        viewModel.updateCurrentModule(args.moduleId)
    }

    private fun initViews() {
        binding.apply {
            tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: Tab?) {
                    stepText.text = resources.getString(R.string.step_module_label,(tab?.position?.plus(
                        1
                    )) , tabLayout.tabCount)
                }
                override fun onTabUnselected(tab: Tab?) {}
                override fun onTabReselected(tab: Tab?) {}
            })
            backButton.setOnClickListener {
                requireActivity().findNavController(R.id.container).popBackStack()
            }
        }
    }

    private fun goToNextStageOrModule(stageIndex: Int) {
        if (stageIndex + 1 < binding.tabLayout.tabCount) {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(stageIndex + 1))
        } else {
            viewModel.getNextModuleIdOrNull()?.let { moduleId ->
                val action = ModuleFragmentDirections.actionGlobalModuleFragment(moduleId)
                requireActivity().findNavController(R.id.container).navigate(action)
            } ?: loadingSnack.show()
        }
    }

    private fun observeModel() {
        viewModel.currentModuleFlow
            .onEach(::updateModuleDisplay)
            .launchWhenStarted(lifecycleScope)

        viewModel.nextStepFlow
            .onEach(::goToNextStageOrModule)
            .launchWhenStarted(lifecycleScope)
    }

    private fun updateModuleDisplay(takesModule: TakesModuleDisplay) {
        binding.apply {
            titleText.text = takesModule.title
            updateStagesDisplay(takesModule.stages)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updateStagesDisplay(stages: List<TakesStage>) {
        binding.apply {
            stagesAdapter = StagesAdapter(this@ModuleFragment).apply {
                updateStages(stages)
            }
            viewPager.adapter = stagesAdapter

            for (stage in stages) {
                val tab = tabLayout.newTab()
                tabLayout.addTab(tab)
            }

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.apply {
                    icon = resources.getDrawable(when (stages[position].type) {
                        StageTypes.Lecture.name -> {
                            R.drawable.lecture_icon
                        }
                        StageTypes.BlockDiagram.name -> {
                            R.drawable.block_diagram_icon
                        }
                        else -> {
                            R.drawable.code_icon
                        }
                    }, requireContext().theme)
                    icon?.setTintList(null)
                    tabLayout.tabIconTint = null
                }
            }.attach()
            viewPager.offscreenPageLimit = stagesAdapter?.itemCount?: 1
        }
    }
}