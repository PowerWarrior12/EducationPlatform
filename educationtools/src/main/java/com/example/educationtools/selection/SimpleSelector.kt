package com.example.educationtools.selection

import android.graphics.*
import com.example.educationtools.base.EditableBlock

val TAG = SimpleSelector::class.simpleName
open class SimpleSelector(
    private val editableBlock: EditableBlock
): Selector {
    private var isSelected: Boolean = false
    private val selectListeners = mutableListOf<Runnable>()

    private val paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    override fun isSelected(): Boolean {
        return isSelected
    }

    override fun select(): Boolean {
        if (isSelected) return false
        isSelected = true
        selectListeners.forEach { listener ->
            listener.run()
        }
        editableBlock.invalidate()
        return true
    }

    override fun deselect(): Boolean {
        if (!isSelected) return false
        isSelected = false
        editableBlock.invalidate()
        return true
    }

    override fun addOnSelectListener(runnable: Runnable) {
        selectListeners.add(runnable)
    }

    override fun drawSelection(canvas: Canvas) {
        if (isSelected) {
            canvas.apply {
                editableBlock.let {editBlock ->
                    drawRect(RectF().apply {
                        left = editBlock.getCenter().x - editBlock.getWidth()/2 - 30f
                        right = editBlock.getCenter().x + editBlock.getWidth()/2 + 30f
                        top = editBlock.getCenter().y - editBlock.getHeight()/2 - 30f
                        bottom = editBlock.getCenter().y + editBlock.getHeight()/2 + 30f
                    }, paint)
                }
            }
        }
    }

    override fun checkPointAvailability(x: Float, y: Float): Boolean {
        return false
    }

    override fun checkPointAvailability(point: PointF): Boolean {
        return checkPointAvailability(point.x, point.y)
    }

    override fun checkAndTouch(x: Float, y: Float) {

    }

    override fun getPriority(): Int {
        return 1
    }
}