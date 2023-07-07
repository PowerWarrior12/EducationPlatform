package com.example.educationplatform.presentation.cource.reports

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.R
import com.example.educationplatform.databinding.CourseReportsFragmentBinding
import com.example.educationplatform.presentation.cource.CourseViewModel
import com.example.educationplatform.presentation.cource.reports.adapter.ReportsAdapter
import com.example.educationplatform.utils.extensions.launchWhenStarted
import kotlinx.coroutines.flow.onEach

class CourseReportsFragment: Fragment(R.layout.course_reports_fragment) {
    private val binding by viewBinding<CourseReportsFragmentBinding>()
    private val viewModel by navGraphViewModels<CourseViewModel>(R.id.course_graph)
    private val reportsAdapter by lazy {
        ReportsAdapter(viewModel::sendReport)
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
            root.adapter = reportsAdapter
        }
    }

    private fun observeModel() {
        viewModel.reportsFlow
            .onEach { reports ->
                reportsAdapter.items = reports
            }.launchWhenStarted(lifecycleScope)
    }

    companion object {
        fun newInstance(): CourseReportsFragment {
            return CourseReportsFragment()
        }
    }
}