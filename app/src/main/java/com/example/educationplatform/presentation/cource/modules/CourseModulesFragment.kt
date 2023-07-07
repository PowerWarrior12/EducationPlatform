package com.example.educationplatform.presentation.cource.modules

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.R
import com.example.educationplatform.databinding.CourseModulesFragmentBinding
import com.example.educationplatform.presentation.cource.CourseFragmentDirections
import com.example.educationplatform.presentation.cource.CourseViewModel
import com.example.educationplatform.presentation.cource.modules.adapter.ModulesAdapter
import com.example.educationplatform.utils.extensions.launchWhenStarted
import kotlinx.coroutines.flow.onEach

class CourseModulesFragment: Fragment(R.layout.course_modules_fragment) {
    private val binding by viewBinding<CourseModulesFragmentBinding>()
    private val viewModel by navGraphViewModels<CourseViewModel>(R.id.course_graph)
    private val modulesAdapter by lazy {
        ModulesAdapter(::openModule)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.apply {
            root.layoutManager = LinearLayoutManager(requireContext())
            root.adapter = modulesAdapter
        }
    }

    private fun observeModel() {
        viewModel.modulesFlow
            .onEach { moduleItems ->
                modulesAdapter.items = moduleItems
            }.launchWhenStarted(lifecycleScope)
    }


    private fun openModule(moduleId: Int) {
        val action = CourseFragmentDirections.actionCourseFragmentToModuleFragment(moduleId)
        requireActivity().findNavController(R.id.container).navigate(action)
    }

    companion object {
        fun newInstance(): CourseModulesFragment {
            return CourseModulesFragment()
        }
    }
}