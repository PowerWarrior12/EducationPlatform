package com.example.educationplatform.presentation.courseEdit.structure.testCases

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationplatform.R
import com.example.educationplatform.databinding.TestCasesFragmentBinding
import com.example.educationplatform.presentation.courseEdit.structure.testCasesAdapter.TestCasesAdapter
import com.example.educationplatform.presentation.courseEdit.structure.testCasesAdapter.TestDataCollector
import com.example.educationplatform.presentation.entities.TestData
import com.example.educationplatform.utils.decorators.MarginDecorator
import com.example.educationplatform.utils.extensions.launchWhenStarted
import kotlinx.coroutines.flow.onEach

class TestCasesFragment: Fragment(R.layout.test_cases_fragment) {
    private val binding by viewBinding<TestCasesFragmentBinding>()
    private val testDataCollector = TestDataCollector()
    private val viewModel by viewModels<TestCasesViewModel>()
    private val testDataAdapter by lazy {
        TestCasesAdapter(viewModel::deleteTestItem, testDataCollector) {
            viewModel.addTestItem()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    fun setTestData(testData: List<TestData>) {
        lifecycleScope.launchWhenStarted {
            viewModel.setTestCases(testData)
        }
    }

    fun getTestData(): List<TestData>? {
        return if (isAdded) viewModel.getTestCases(testDataCollector)
        else null
    }

    private fun observeModel() {
        viewModel.testFlow
            .onEach { testDataItems ->
                testDataAdapter.items = testDataItems
            }.launchWhenStarted(lifecycleScope)
    }

    private fun initViews() {
        binding.root.apply {
            adapter = testDataAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(MarginDecorator(listOf(0, 1), left = 64, right = 64, top = 64, bottom = 64))
        }
    }

    companion object {
        fun newInstance(): TestCasesFragment {
            return TestCasesFragment()
        }
    }
}