package com.example.educationplatform.presentation.catalog

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.MainApplication
import com.example.educationplatform.MainGraphDirections
import com.example.educationplatform.R
import com.example.educationplatform.databinding.CatalogFragmentBinding
import com.example.educationplatform.presentation.catalog.adapter.CoursesAdapter
import com.example.educationplatform.utils.decorators.MarginDecorator
import com.example.educationplatform.utils.extensions.launchWhenStarted
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class CatalogFragment: Fragment(R.layout.catalog_fragment) {
    @Inject
    lateinit var viewModelFactory: CatalogViewModel.CatalogViewModelFactory

    private val binding by viewBinding<CatalogFragmentBinding>()
    private val viewModel by viewModels<CatalogViewModel> {
        viewModelFactory
    }
    private val coursesAdapter = CoursesAdapter(::openCourse)

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.appComponent.inject(this)
        super.onAttach(context)
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
        binding.recyclerView.apply {
            adapter = coursesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(MarginDecorator(listOf(0, 1), left = 30, top = 30, bottom = 30, right = 30))
        }
        binding.swipeLayout.setOnRefreshListener {
            viewModel.loadCourses()
            binding.swipeLayout.isRefreshing = false
        }
    }

    private fun observeModel() {
        viewModel.courseFlow
            .onEach { coursesAdapter.items = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.errorFlow
            .onEach { isError ->
                binding.errorLayout.root.isVisible = isError
            }.launchWhenStarted(lifecycleScope)

        viewModel.processFlow
            .onEach { isProcess ->
                binding.swipeLayout.isEnabled = !isProcess
            }.launchWhenStarted(lifecycleScope)
    }

    private fun openCourse(courseId: Int) {
        val action = MainGraphDirections.actionGlobalCourseFragment(courseId)
        requireActivity().findNavController(R.id.container).navigate(action)
    }
}