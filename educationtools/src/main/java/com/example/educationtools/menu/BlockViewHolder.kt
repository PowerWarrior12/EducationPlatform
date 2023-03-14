package com.example.educationtools.menu

import android.content.ClipData
import android.content.ClipDescription
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.educationtools.databinding.BlockMenuItemBinding

class BlockViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding by viewBinding<BlockMenuItemBinding>()
    fun bind(blockMenuItem: BlockMenuItem) {
        binding.imageView.setImageResource(blockMenuItem.icon)
        binding.title.text = blockMenuItem.title
        binding.root.setOnLongClickListener { view ->
            val item = ClipData.Item(blockMenuItem.blockType)
            val dataToDrag = ClipData(
                "drag",
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )

            val maskShadow = MaskShadowBuilder(view, blockMenuItem.icon)

            //supports Nougat and beyond
            view.startDragAndDrop(dataToDrag, maskShadow, view, 0)
            true
        }
    }
}