package com.example.educationtools.menu

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

object BlockItemDiffCallback: DiffUtil.ItemCallback<BlockMenuItem>() {
    override fun areItemsTheSame(oldItem: BlockMenuItem, newItem: BlockMenuItem): Boolean {
        return oldItem === newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: BlockMenuItem, newItem: BlockMenuItem): Boolean {
        return oldItem == newItem
    }
}