package com.example.educationtools.base

import android.graphics.*

const val defaultCenterX = 0f
const val defaultCenterY = 0f

abstract class EditableBlockBase() : EditableBlock {
    //Sizes
    private val center: PointF = PointF(defaultCenterX, defaultCenterY)
    private var width = 0f
    private var height = 0f

    private var selector: Selector? = null

    //Main rect
    private val mainRect = RectF(
        center.x - width / 2,
        center.y - height / 2,
        center.x + width / 2,
        center.y + height / 2
    )

    override fun updatePosition(newPosition: PointF) {
        center.apply {
            x = newPosition.x
            y = newPosition.y
        }
        updateMainRect()
    }

    override fun updateSize(newWidth: Float, newHeight: Float) {
        width = newWidth
        height = newHeight
        updateMainRect()
    }

    override fun getSelector(): Selector? {
        return selector
    }

    private fun updateMainRect() {
        mainRect.apply {
            left = center.x - width/2
            right = center.x + width/2
            top = center.y - height/2
            bottom = center.y + height/2
        }
    }

    abstract inner class Builder() {
        val config = EditableBlockConfig()
        var selector: Selector? = null
        var invalidate: Runnable? = null

        fun setSelector(selector: Selector): Builder {
            this@EditableBlockBase.selector = selector
            selector.setEditableBlock(this@EditableBlockBase)
            return this
        }

        fun setSize(width: Float, height: Float): Builder {
            config.height = height
            config.width = width
            return this
        }

        fun setPosition(centerX: Float, centerY: Float): Builder {
            config.centerX = centerX
            config.centerY = centerY
            return this
        }

        abstract fun build(): EditableBlock
    }

    data class EditableBlockConfig(
        var centerX: Float = 0f,
        var centerY: Float = 0f,
        var width: Float = 0f,
        var height: Float = 0f,
    ) {

    }
}