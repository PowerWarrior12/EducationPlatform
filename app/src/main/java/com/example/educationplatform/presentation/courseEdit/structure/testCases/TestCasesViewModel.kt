package com.example.educationplatform.presentation.courseEdit.structure.testCases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educationplatform.presentation.courseEdit.structure.testCasesAdapter.TestDataCollector
import com.example.educationplatform.presentation.courseEdit.structure.testCasesAdapter.TestDataItem
import com.example.educationplatform.presentation.entities.TestData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestCasesViewModel: ViewModel() {
    private val _testFlow = MutableStateFlow<List<TestDataItem>>(
        listOf(
            TestDataItem.AddTestDataItem()
        )
    )

    val testFlow = _testFlow.asStateFlow()

    fun setTestCases(testCases: List<TestData>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val testItems = testCases.mapIndexed { index, testData ->
                    TestDataItem.TestDataBaseItem(testData, index)
                }.toMutableList<TestDataItem>()
                testItems.add(TestDataItem.AddTestDataItem())
                _testFlow.emit(testItems)
            }
        }
    }

    fun getTestCases(testDataCollector: TestDataCollector): List<TestData> {
        /*val testItems = testFlow.value.dropLast(1)
        return testItems.map { testItem ->
            (testItem as TestDataItem.TestDataBaseItem).testData
        }*/return testDataCollector.getData()
    }

    fun addTestItem() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val testItems = _testFlow.value.toMutableList()
                testItems.add(testItems.count() - 1, TestDataItem.TestDataBaseItem(TestData("", ""), testItems.count()))
                _testFlow.emit(testItems)
            }
        }
    }

    fun deleteTestItem(number: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val testItems = _testFlow.value.toMutableList()
                testItems.removeAt(number - 1)
                val newTestItems = testItems.mapIndexed { index, testDataItem ->
                    if (testDataItem is TestDataItem.TestDataBaseItem) {
                        testDataItem.number = index + 1
                        testDataItem
                    } else {
                        testDataItem
                    }
                }
                _testFlow.emit(newTestItems)
            }
        }
    }
}