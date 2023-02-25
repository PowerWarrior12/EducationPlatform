package com.example.educationtools.dragging

import android.graphics.PointF
import android.util.Log
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
    private val lastMovePoint = PointF()

    private fun drag(touchInfo: TouchManager.TouchInfo) {
        if (touchInfo is TouchManager.TouchInfo.FilledInfo) {
            val touchable = touchInfo.touchable
            if (touchable is EditableBlock) {
                draggedBlock = touchable
                lastMovePoint.apply {
                    x = touchInfo.xPos
                    y = touchInfo.yPos
                }
            }
        }
    }

    private fun dragProcess(touchInfo: TouchManager.TouchInfo) {
        draggedBlock?.let { block ->
            val center = block.getCenter()
            Log.d(DragAndDropManager::class.simpleName, "center: $center")
            val newPosition = PointF().apply {
                x = center.x - (lastMovePoint.x - touchInfo.xPos)
                y = center.y - (lastMovePoint.y - touchInfo.yPos)
            }
            Log.d(DragAndDropManager::class.simpleName, "newPosition: $newPosition, lastMoveP: $lastMovePoint, touchInfo: $touchInfo")
            lastMovePoint.apply {
                x = touchInfo.xPos
                y = touchInfo.yPos
            }
            Log.d(DragAndDropManager::class.simpleName, "center")
            block.updatePosition(newPosition)
        }
    }

    private fun drop(touchInfo: TouchManager.TouchInfo) {
        draggedBlock = null
    }
}