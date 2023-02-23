package com.example.educationtools.base

import android.graphics.Canvas
import android.graphics.Point
import android.graphics.PointF
import android.graphics.RectF

interface EditableBlock {
    /**
     * Отрисовка фигуры на переданном канвасе
     */
    fun draw(canvas: Canvas)

    /**
     * Проверка принадлежности точки блоку
     * @param point Точка, принадлежность области блоку которой необходимо проверить
     */
    fun checkPointConsists(point: PointF): Boolean
    fun setEditorParent(parentEditor: ParentEditor)
    fun updatePosition(newPosition: PointF)
    fun updateSize(newWidth: Float, newHeight: Float)
    fun getSelector(): Selector
    fun setText(newText: String)
    fun getCenter(): PointF
    fun getWidth(): Float
    fun getHeight(): Float
    fun getText(): String
    fun invalidate()
}