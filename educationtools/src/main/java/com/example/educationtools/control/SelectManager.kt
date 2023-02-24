package com.example.educationtools.control

import android.app.usage.UsageEvents.Event
import android.graphics.PointF
import android.view.MotionEvent
import com.example.educationtools.base.EditableBlock

class SelectManager(
    private val editable: MutableList<EditableBlock>
) {
    private var selectedEditable: EditableBlock? = null
    private val longPressListeners = mutableListOf<(EditableBlock) -> Unit>()

    fun addEditable(editableBlock: EditableBlock) {
        editable.add(editableBlock)
    }

    fun addLongPressListener(listener: (EditableBlock) -> Unit) {
        longPressListeners.add(listener)
    }

    fun onSingleTap(pointF: PointF) {
        if (processSelected(pointF))
        else if (processEditable(pointF))
        else {
            selectedEditable?.let { selectedEditable ->
                val selector = selectedEditable.getSelector()
                selector.deselect()
                this.selectedEditable = null
            }
        }
    }

    fun onLongPress(pointF: PointF) {
        this.editable.firstOrNull { block ->
            block.checkPointConsists(pointF)
        }?.let { block ->
            longPressListeners.forEach { listener ->
                listener(block)
            }
        }
    }

    private fun processEditable(pointF: PointF): Boolean {
        for (block in editable) {
            val selector = block.getSelector()
            if (block.checkPointConsists(pointF)) {
                selectedEditable = if (block == selectedEditable) {
                    selector.deselect()
                    null
                } else {
                    selectedEditable?.getSelector()?.deselect()
                    selector.select()
                    block
                }
                return true
            }
        }
        return false
    }

    private fun processSelected(pointF: PointF): Boolean {
        selectedEditable?.let { selectedEditable ->
            val selector = selectedEditable.getSelector()
            if (selector.checkPointConsists(pointF) && selector.onSingleTap(pointF)) {
                return true
            }
        }
        return false
    }

    fun onScroll() {

    }
}