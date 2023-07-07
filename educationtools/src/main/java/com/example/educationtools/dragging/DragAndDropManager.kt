package com.example.educationtools.dragging

import android.graphics.PointF
import com.example.educationtools.base.EditableBlock
import com.example.educationtools.touching.TouchManager
import com.example.educationtools.utils.Transformations

class DragAndDropManager(private val touchManager: TouchManager, private val tranformations: Transformations) {
    fun start() {
        touchManager.addLongTouchListener(::drag)
        touchManager.addMoveListener(::dragProcess)
        touchManager.addTouchReleaseListener(::drop)
    }

    private var draggedBlock: EditableBlock? = null
    private var offset = PointF()

    private fun drag(touchInfo: TouchManager.TouchInfo) {
        if (touchInfo is TouchManager.TouchInfo.FilledInfo) {
            val touchable = touchInfo.touchable
            if (touchable is EditableBlock) {
                draggedBlock = touchable

                offset.apply {
                    x = touchable.getCenter().x - touchInfo.xPos
                    y = touchable.getCenter().y - touchInfo.yPos
                }
            }
        }
    }

    private fun dragProcess(touchInfo: TouchManager.TouchInfo) {
        draggedBlock?.let { block ->
            val newPosition = PointF().apply {
                x = touchInfo.xPos + offset.x
                y = touchInfo.yPos + offset.y
            }
            block.updatePosition(newPosition)
        }
    }

    private fun drop(touchInfo: TouchManager.TouchInfo) {
        draggedBlock = null
    }
}