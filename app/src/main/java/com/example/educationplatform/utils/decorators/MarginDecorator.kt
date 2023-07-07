package com.example.educationplatform.utils.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginDecorator(
    private val viewTypes: List<Int>,
    private val left: Int = 0,
    private val right: Int = 0,
    private val top: Int = 0,
    private val bottom: Int = 0
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (!viewTypes.contains(parent.getChildViewHolder(view).itemViewType)) return
        with(outRect) {
            left = this@MarginDecorator.left
            top = this@MarginDecorator.top
            bottom = this@MarginDecorator.bottom
            right = this@MarginDecorator.right
        }
    }

}