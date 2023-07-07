package com.example.educationtools.selection

import android.graphics.Canvas
import com.example.educationtools.touching.TouchManager
import com.example.educationtools.touching.Touchable

interface Selector: Touchable {
    fun isSelected(): Boolean
    fun select(): Boolean
    fun deselect(): Boolean
    fun addOnSelectListener(runnable: Runnable)
    fun addOnDeselectListener(runnable: Runnable)
    fun drawSelection(canvas: Canvas)
    fun move(touchInfo: TouchManager.TouchInfo)
    fun touch(touchInfo: TouchManager.TouchInfo)
    fun update()
}