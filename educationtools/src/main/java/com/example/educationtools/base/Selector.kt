package com.example.educationtools.base

import android.graphics.Canvas
import android.graphics.PointF

interface Selector {
    fun setEditableBlock(block: EditableBlock)
    fun select(): Boolean
    fun deselect(): Boolean
    fun addOnSelectListener(runnable: Runnable)
    fun drawSelection(canvas: Canvas)
    fun checkPointConsists(point: PointF)
    fun onTouchDown(point: PointF)
    fun onTouchUp(point: PointF)
    fun onScroll(distances: PointF)
}