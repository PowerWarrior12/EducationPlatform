package com.example.educationplatform.utils.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.lang.Integer.min

class StickyFooterItemDecoration(
    private val minVerticalMargin: Int,
    private val viewTypes: List<Int>
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapterItemCount: Int = parent.adapter!!.itemCount
        if (isFooter(parent, view, adapterItemCount)) {
            if (view.height == 0 && state.didStructureChange()) {
                hideFooterAndUpdate(outRect, view, parent)
            } else {
                outRect.set(0, calculateTopOffset(parent, view, adapterItemCount), 0, minVerticalMargin)
            }
        }
    }

    private fun hideFooterAndUpdate(outRect: Rect, footerView: View, parent: RecyclerView) {
        outRect.set(0, OFF_SCREEN_OFFSET, 0, 0)
        footerView.post {
            parent.adapter?.notifyDataSetChanged()
        }
    }

    private fun calculateTopOffset(
        parent: RecyclerView,
        footerView: View,
        itemCount: Int
    ): Int {
        val topOffset: Int =
            parent.height - visibleChildHeightWithFooter(parent, footerView, itemCount)
        return (if (topOffset < 0) 0 else topOffset).coerceAtLeast(minVerticalMargin)
    }

    private fun visibleChildHeightWithFooter(
        parent: RecyclerView,
        footerView: View,
        itemCount: Int
    ): Int {
        var totalHeight = 0
        val onScreenItemCount = min(parent.childCount, itemCount)
        for (i in 0 until onScreenItemCount - 1) {
            totalHeight += parent.getChildAt(i).measuredHeight
        }
        totalHeight += footerView.measuredHeight

        return totalHeight + minVerticalMargin
    }

    private fun isFooter(parent: RecyclerView, view: View, itemCount: Int): Boolean {
        return parent.getChildAdapterPosition(view) == itemCount - 1 && viewTypes.contains(
            parent.getChildViewHolder(
                view
            ).itemViewType
        )
    }

    companion object {

        private const val OFF_SCREEN_OFFSET = 5000
    }
}