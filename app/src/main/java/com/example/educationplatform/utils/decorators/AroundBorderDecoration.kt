package com.example.educationplatform.utils.decorators

import android.graphics.*
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.State

class AroundBorderDecoration(): RecyclerView.ItemDecoration() {

    private val borderPaint = Paint().apply {
        color = Color.GRAY
        isAntiAlias = true
        strokeWidth = 3f
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(30f, 10f), 0f)
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        for (i in 0 until parent.childCount) {
            draw(parent.getChildAt(i), c, parent, state)
        }
    }

    private fun draw(view: View, canvas: Canvas, recyclerView: RecyclerView, state: State) {
        when(recyclerView.getChildViewHolder(view).itemViewType) {
            0, 2 -> {
                val viewRect = Rect(0, view.top, recyclerView.width, view.bottom - 3)
                canvas.drawRect(viewRect, borderPaint)
            }
            else -> {
                val path = Path().apply {
                    moveTo(0f, view.top.toFloat())
                    lineTo(0f, view.bottom.toFloat())
                    moveTo(recyclerView.width.toFloat(), view.top.toFloat())
                    lineTo(recyclerView.width.toFloat(), view.bottom.toFloat())
                }
                canvas.drawPath(path, borderPaint)
            }
        }
    }
}