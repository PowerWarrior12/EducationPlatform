package com.example.educationplatform.presentation.courseEdit.structure.testCasesAdapter

import android.view.View.OnClickListener
import com.example.educationplatform.R
import com.example.educationplatform.databinding.ButtonItemBinding
import com.example.educationplatform.databinding.TestCaseItemBinding
import com.example.educationplatform.presentation.entities.TestData
import com.example.educationplatform.utils.DataCollector
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun testCaseAdapter(onDeleteClick: (number: Int) -> Unit, dataCollector: DataCollector<TestData>) =
    adapterDelegateViewBinding<TestDataItem.TestDataBaseItem, TestDataItem, TestCaseItemBinding>(
        { layoutInflater, root -> TestCaseItemBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.apply {

                fun getTestCase(): TestData {
                    return TestData(inputData.text.toString(), outputData.text.toString())
                }

                inputData.setText(item.testData.inputData)
                outputData.setText(item.testData.outputData)

                dataCollector.addDataSource(::getTestCase)

                deleteButton.setOnClickListener {
                    dataCollector.deleteDataSource(::getTestCase)
                    onDeleteClick(item.number)
                }

                testCaseNumber.text = "${binding.root.resources.getString(R.string.test_case_label)}${item.number}"
            }
        }
    }

fun addTestCaseAdapter(onClick: OnClickListener) =
    adapterDelegateViewBinding<TestDataItem.AddTestDataItem, TestDataItem, ButtonItemBinding>(
        { layoutInflater, root -> ButtonItemBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            binding.apply {
                button.text = binding.root.resources.getString(R.string.add_test_case_button)
                button.setOnClickListener(onClick)
            }
        }
    }