package com.example.educationplatform.presentation.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.MainApplication
import com.example.educationplatform.MainGraphDirections
import com.example.educationplatform.R
import com.example.educationplatform.databinding.HomeFragmentBinding
import com.example.educationplatform.presentation.home.mainAdapter.UsersCoursesAdapter
import com.example.educationplatform.utils.decorators.MarginDecorator
import com.example.educationplatform.utils.extensions.launchWhenStarted
import com.example.educationplatform.utils.layoutManagers.FooterLayoutManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.home_fragment) {
    private val binding by viewBinding<HomeFragmentBinding>()
    private val adapter = UsersCoursesAdapter({ id ->
        val action = MainGraphDirections.actionGlobalCourseFragment(id)
        requireActivity().findNavController(R.id.container).navigate(action)
    }, { id ->
        val action = MainGraphDirections.actionGlobalCourseEditFragment(id)
        requireActivity().findNavController(R.id.container).navigate(action)
    }, {
        val action = MainGraphDirections.actionGlobalCourseCreateFragment()
        requireActivity().findNavController(R.id.container).navigate(action)
    })
    private val viewModel by viewModels<HomeViewModel> {
        homeViewModelFactory.create()
    }

    @Inject
    lateinit var homeViewModelFactory: HomeViewModel.HomeViewModelFactory.Factory

    private var observeJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val x = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeJob = lifecycleScope.launch {
            observeModel()
        }
    }

    private fun observeModel() {
        viewModel.takesCoursesFlow
            .onEach {
                adapter.items = it
            }
            .launchWhenStarted(lifecycleScope)
        viewModel.errorFlow
            .onEach { error ->
                binding.errorLayout.root.isVisible = error
            }
            .launchWhenStarted(lifecycleScope)
        viewModel.processFlow
            .dropWhile { !it }
            .onEach {
                binding.swipeLayout.isEnabled = !it
                binding.loadingBar.isVisible = it
            }
            .launchWhenStarted(lifecycleScope)
    }

    private fun initViews() {
        binding.recyclerView.apply {
            layoutManager = FooterLayoutManager(3, context)
            adapter = this@HomeFragment.adapter
            addItemDecoration(MarginDecorator(listOf(0), left = 30, top = 30, bottom = 30))
            addItemDecoration(
                MarginDecorator(
                    listOf(1, 3, 4),
                    left = 60,
                    top = 30,
                    bottom = 30,
                    right = 60
                )
            )
        }
        binding.apply {
            swipeLayout.setOnRefreshListener {
                viewModel.updateCourses()
                swipeLayout.isRefreshing = false
            }
            errorLayout.tryAgainButton.setOnClickListener {
                viewModel.updateCourses()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        observeJob?.cancel()
    }

    override fun onAttach(context: Context) {
        MainApplication.INSTANCE.appComponent.inject(this)
        super.onAttach(context)
    }
}