package com.example.educationtools.control

import android.graphics.*
import android.util.Log
import com.example.educationtools.base.EditableBlock
import com.example.educationtools.base.Selector

val TAG = SimpleSelector::class.simpleName
const val missingEditBlock = "Editable block is null"
open class SimpleSelector: Selector {
    private var isSelected: Boolean = false
    private var editableBlock: EditableBlock? = null
    private val selectListeners = mutableListOf<Runnable>()

    private val paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    override fun setEditableBlock(block: EditableBlock) {
        this.editableBlock = block
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
        editableBlock?.invalidate() ?: Log.d(TAG, missingEditBlock)
        return true
    }

    override fun deselect(): Boolean {
        if (!isSelected) return false
        isSelected = false
        editableBlock?.invalidate() ?: Log.d(TAG, missingEditBlock)
        return true
    }

    override fun addOnSelectListener(runnable: Runnable) {
        selectListeners.add(runnable)
    }

    override fun drawSelection(canvas: Canvas) {
        if (isSelected) {
            canvas.apply {
                editableBlock?.let {editBlock ->
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

    override fun checkPointConsists(point: PointF): Boolean {
        return false
    }

    override fun onSingleTap(point: PointF): Boolean {
        return false
    }

    override fun onScroll(distances: PointF): Boolean {
        return false
    }
}