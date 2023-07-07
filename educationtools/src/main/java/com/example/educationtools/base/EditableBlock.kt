package com.example.educationtools.base

import android.graphics.Canvas
import android.graphics.PointF

interface EditableBlock {
    fun draw(canvas: Canvas)
    fun setEditorParent(parentEditor: ParentEditor)
    fun updatePosition(newPosition: PointF)
    fun updateSize(newWidth: Float, newHeight: Float)
    fun setText(newText: String)
    fun getCenter(): PointF
    fun getWidth(): Float
    fun getHeight(): Float
    fun getText(): String
    fun deleteBlock()
    fun invalidate()
    fun getGridSize(): Float
    fun getScale(): Float
    val configuration: EditableBlockFactory
}