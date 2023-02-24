package com.example.educationtools.control

import android.graphics.PointF
import android.util.Log
import com.example.educationtools.base.EditableBlock

class DragAndDropManager {

    var draggedBlock: EditableBlock? = null

    fun drag(editableBlock: EditableBlock) {
        draggedBlock = editableBlock
        Log.d(DragAndDropManager::class.simpleName, "drag complete")
    }

    fun dragProcess(currentX: Float, currentY: Float) {
        draggedBlock?.let { block ->
            block.updatePosition(PointF(currentX, currentY))
            Log.d(DragAndDropManager::class.simpleName, "${block} + x: ${currentX}, y: ${currentY}")
        }
    }

    fun drop() {
        draggedBlock = null
    }
}