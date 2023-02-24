package com.example.educationtools.base

import android.graphics.Canvas
import android.graphics.PointF

interface Selector: Touchable {
    fun isSelected(): Boolean
    fun select(): Boolean
    fun deselect(): Boolean
    fun addOnSelectListener(runnable: Runnable)
    fun drawSelection(canvas: Canvas)
}