package com.example.educationtools.base

import android.graphics.Canvas
import android.graphics.PointF

interface Selector {
    fun setEditableBlock(block: EditableBlock)
    fun isSelected(): Boolean
    fun select(): Boolean
    fun deselect(): Boolean
    fun addOnSelectListener(runnable: Runnable)
    fun drawSelection(canvas: Canvas)
    fun checkPointConsists(point: PointF): Boolean
    fun onSingleTap(point: PointF): Boolean
    fun onScroll(distances: PointF): Boolean
}