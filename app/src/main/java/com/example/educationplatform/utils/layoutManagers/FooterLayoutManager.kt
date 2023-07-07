package com.example.educationplatform.utils.layoutManagers

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class FooterLayoutManager(private val footerType: Int, context: Context?) :
    LinearLayoutManager(context) {

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        this.recyclerView = view
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)

        recyclerView?.let { recyclerView ->
            val footer = getChildAt(childCount - 1) ?: return
            val lastChildBottom = getChildAt(childCount - 2)?.bottom ?: paddingTop
            if (checkIsFooter(footer, recyclerView)) {
                if (lastChildBottom < height) {
                    val decorations = Rect()
                    calculateItemDecorationsForChild(footer, decorations)
                    val footerHeight = footer.height + decorations.top + decorations.bottom
                    val top = (height - footerHeight).coerceAtLeast(lastChildBottom)
                    val bottom = top + footerHeight
                    val left = getDecoratedLeft(footer)
                    val right = getDecoratedRight(footer)
                    layoutDecoratedWithMargins(footer, left, top, right, bottom)
                }
            }
        }
    }

    private fun checkIsFooter(view: View, parent: RecyclerView): Boolean {
        return parent.getChildViewHolder(view).itemViewType == footerType
    }
}