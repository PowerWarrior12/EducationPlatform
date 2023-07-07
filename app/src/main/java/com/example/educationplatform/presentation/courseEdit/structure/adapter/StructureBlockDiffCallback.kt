package com.example.educationplatform.presentation.courseEdit.structure.adapter

import androidx.recyclerview.widget.DiffUtil

object StructureBlockDiffCallback: DiffUtil.ItemCallback<StructureBlockItem>() {
    override fun areItemsTheSame(
        oldItem: StructureBlockItem,
        newItem: StructureBlockItem
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: StructureBlockItem,
        newItem: StructureBlockItem
    ): Boolean {
        return oldItem == newItem
    }
}