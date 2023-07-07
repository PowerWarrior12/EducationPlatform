package com.example.educationtools.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.educationtools.R

class BlocksAdapter: ListAdapter<BlockMenuItem, BlockViewHolder>(BlockItemDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockViewHolder {
        return BlockViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.block_menu_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BlockViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}