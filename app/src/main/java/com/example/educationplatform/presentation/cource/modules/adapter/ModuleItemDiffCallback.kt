package com.example.educationplatform.presentation.cource.modules.adapter

import androidx.recyclerview.widget.DiffUtil

object ModuleItemDiffCallback: DiffUtil.ItemCallback<ModuleItem>() {
    override fun areItemsTheSame(
        oldItem: ModuleItem,
        newItem: ModuleItem
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: ModuleItem,
        newItem: ModuleItem
    ): Boolean {
        return oldItem == newItem
    }
}