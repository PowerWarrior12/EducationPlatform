package com.example.educationtools.control

import android.graphics.Canvas
import android.graphics.PointF
import com.example.educationtools.base.EditableBlock
import com.example.educationtools.base.Selector

class SimpleSelector: Selector {
    override fun setEditableBlock(block: EditableBlock) {
        TODO("Not yet implemented")
    }

    override fun select(): Boolean {
        TODO("Not yet implemented")
    }

    override fun deselect(): Boolean {
        TODO("Not yet implemented")
    }

    override fun addOnSelectListener(runnable: Runnable) {
        TODO("Not yet implemented")
    }

    override fun drawSelection(canvas: Canvas) {
        TODO("Not yet implemented")
    }

    override fun checkPointConsists(point: PointF) {
        TODO("Not yet implemented")
    }

    override fun onTouchDown(point: PointF) {
        TODO("Not yet implemented")
    }

    override fun onTouchUp(point: PointF) {
        TODO("Not yet implemented")
    }

    override fun onScroll(distances: PointF) {
        TODO("Not yet implemented")
    }
}