package com.example.educationplatform.presentation.courseEdit

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.MainApplication
import com.example.educationplatform.R
import com.example.educationplatform.databinding.CourseEditFragmentBinding
import com.example.educationplatform.utils.extensions.launchWhenStarted
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class CourseEditFragment : Fragment(R.layout.course_edit_fragment) {
    @Inject
    lateinit var viewModelFactory: CourseEditViewModel.CourseEditViewModelFactory.Factory

    private val args: CourseEditFragmentArgs by navArgs()
    private val binding by viewBinding<CourseEditFragmentBinding>()
    private val viewModel by navGraphViewModels<CourseEditViewModel>(R.id.edit_course_graph) {
        viewModelFactory.create(args.courseId)
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

    private fun initViews() {
        binding.apply {
            viewPager.adapter = CourseEditAdapter(this@CourseEditFragment)
            toolbar.actionButton.setOnClickListener {
                requireActivity().findNavController(R.id.container).popBackStack()
            }
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when(position) {
                    0 -> tab.text = resources.getText(R.string.info_tab)
                    1 -> tab.text = resources.getText(R.string.structure_tab)
                    else -> tab.text = resources.getText(R.string.state_tab)
                }
            }.attach()
        }
    }

    private fun observeModel() {
        viewModel.courseFlow
            .onEach { course ->
                binding.toolbar.registrationLabel.text = course.title
            }.launchWhenStarted(lifecycleScope)
    }
}