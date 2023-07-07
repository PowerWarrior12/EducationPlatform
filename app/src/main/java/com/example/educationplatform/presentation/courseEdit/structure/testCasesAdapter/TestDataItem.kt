package com.example.educationplatform.presentation.courseEdit.structure.testCasesAdapter

import com.example.educationplatform.presentation.entities.TestData

sealed class TestDataItem {
    class TestDataBaseItem(val testData: TestData, var number: Int): TestDataItem()
    class AddTestDataItem(): TestDataItem()
}