package com.example.educationplatform.presentation.courseEdit.structure.testCasesAdapter

import com.example.educationplatform.presentation.entities.TestData
import com.example.educationplatform.utils.DataCollector

class TestDataCollector: DataCollector<TestData> {
    private val dataSources = mutableListOf<() -> TestData>()

    override fun addDataSource(dataSource: () -> TestData) {
        dataSources.add(dataSource)
    }

    override fun deleteDataSource(dataSource: () -> TestData) {
        dataSources.remove(dataSource)
    }

    fun getData(): List<TestData> {
        return dataSources.map { dataSource ->
            dataSource()
        }
    }
}