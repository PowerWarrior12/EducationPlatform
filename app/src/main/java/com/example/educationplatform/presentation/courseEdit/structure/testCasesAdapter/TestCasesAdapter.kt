package com.example.educationplatform.presentation.courseEdit.structure.testCasesAdapter

import android.view.View
import com.example.educationplatform.presentation.entities.TestData
import com.example.educationplatform.utils.DataCollector
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class TestCasesAdapter(
    onDeleteClick: (number: Int) -> Unit,
    dataCollector: DataCollector<TestData>,
    addTestCase: View.OnClickListener
): AsyncListDifferDelegationAdapter<TestDataItem>(
    TestDataDiffCallback,
    testCaseAdapter(onDeleteClick, dataCollector),
    addTestCaseAdapter(addTestCase)
)