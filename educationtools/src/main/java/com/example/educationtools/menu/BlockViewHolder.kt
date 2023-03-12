package com.example.educationtools.menu

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationtools.databinding.BlockMenuItemBinding

class BlockViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding by viewBinding<BlockMenuItemBinding>()

    fun bind(blockMenuItem: BlockMenuItem) {
        binding.imageView.setImageResource(blockMenuItem.icon)
        binding.title.text = blockMenuItem.title
    }
}