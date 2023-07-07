package com.example.educationplatform.presentation.cource

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.educationplatform.MainApplication
import com.example.educationplatform.R
import com.example.educationplatform.databinding.CourceFragmentBinding
import com.example.educationplatform.domain.entities.TakesModule
import com.example.educationplatform.presentation.cource.module.ModulesViewModel
import com.example.educationplatform.presentation.cource.modules.adapter.ModuleItem
import com.example.educationplatform.utils.extensions.doOnFirst
import com.example.educationplatform.utils.extensions.launchWhenStarted
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class CourseFragment: Fragment(R.layout.cource_fragment) {
    @Inject
    lateinit var viewModelFactory: CourseViewModel.CourseViewModelFactory.Factory
    @Inject
    lateinit var modulesViewModelFactory: ModulesViewModel.ModulesViewModelFactory
    private val args by navArgs<CourseFragmentArgs>()
    private val viewModel by navGraphViewModels<CourseViewModel>(R.id.course_graph) {
        viewModelFactory.create(args.courseId)
    }
    private val modulesViewModel by navGraphViewModels<ModulesViewModel>(R.id.course_graph) {
        modulesViewModelFactory
    }
    private val binding by viewBinding<CourceFragmentBinding>()
    private var courseAdapter : CourseAdapter? = null
    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        job = lifecycleScope.launch {
            observeModel()
        }
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
    }

    private fun initViews() {
        binding.apply {
            courseAdapter = CourseAdapter(this@CourseFragment)
            viewPager.adapter = courseAdapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when(position) {
                    0 -> tab.text = resources.getText(R.string.info_tab)
                    1 -> tab.text = resources.getText(R.string.reports_label)
                    else -> tab.text = resources.getText(R.string.modules_label)
                }
            }.attach()

            submitButton.setOnClickListener {
                viewModel.subscribeProcess()
            }
        }
    }

    private fun observeModel() {
        viewModel.courseFlow
            .onEach { course ->
                Glide
                    .with(binding.root)
                    .load(course.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.teacher)
                    .into(binding.icon)

                binding.titleText.text = course.title
            }.launchWhenStarted(lifecycleScope)

        viewModel.modulesFlow
            .onEach { moduleItems ->
                modulesViewModel.setModules(moduleItemsToTakeModules(moduleItems))
            }.launchWhenStarted(lifecycleScope)

        viewModel.subscriptionFlow
            .doOnFirst {
                binding.submitButton.isVisible = true
            }
            .onEach { subscription ->
                binding.submitButton.text = if (subscription) {
                    getString(R.string.unsubscribe_label)
                } else {
                    getString(R.string.submit_label)
                }
            }.launchWhenStarted(lifecycleScope)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MainApplication.INSTANCE.appComponent.inject(this)
    }

    private fun moduleItemsToTakeModules(moduleItems: List<ModuleItem>): List<TakesModule> {
        return moduleItems.map { moduleItem ->
            (moduleItem as ModuleItem.TakesModuleItem).module
        }
    }
}