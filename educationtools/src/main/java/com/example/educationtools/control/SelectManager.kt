package com.example.educationtools.control

import android.app.usage.UsageEvents.Event
import android.graphics.PointF
import android.view.MotionEvent
import com.example.educationtools.base.EditableBlock
import com.example.educationtools.base.Selectable
import com.example.educationtools.base.Selector
import com.example.educationtools.base.Touchable

class SelectManager(
    private val touchManager: TouchManager
) {
    private var currentSelectable: Selectable? = null

    fun start() {
        touchManager.addSingleTouchListener(::onSingleTap)
    }

    private fun onSingleTap(touchInfo: TouchManager.TouchInfo) {
        if (touchInfo.touchable is Selector) {
            processSelector(touchInfo.touchable)
        } else if (touchInfo.touchable is Selectable) {
            processSelectable(touchInfo.touchable)
        }
    }

    private fun processSelectable(selectable: Selectable) {
        val selector = selectable.getSelector()
        if (selector.isSelected()) {
            selector.deselect()
            currentSelectable = null
            touchManager.deleteTouchable(selector)
        } else {
            selector.select()
            currentSelectable = selectable
            touchManager.addTouchable(selector)
        }
    }

    private fun processSelector(selector: Selector) {

    }
}