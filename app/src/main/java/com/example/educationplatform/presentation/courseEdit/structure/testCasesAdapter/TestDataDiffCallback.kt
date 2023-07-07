package com.example.educationplatform.presentation.courseEdit.structure.testCasesAdapter

import androidx.recyclerview.widget.DiffUtil

object TestDataDiffCallback: DiffUtil.ItemCallback<TestDataItem>() {
    override fun areItemsTheSame(oldItem: TestDataItem, newItem: TestDataItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: TestDataItem, newItem: TestDataItem): Boolean {
        return oldItem == newItem
    }
}