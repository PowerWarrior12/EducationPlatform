package com.example.educationtools.selection

import com.example.educationtools.touching.TouchManager

class SelectManager(
    private val touchManager: TouchManager
) {
    private var currentSelectable: Selectable? = null

    fun start() {
        touchManager.addSingleTouchListener(::onSingleTap)
        touchManager.addTouchListener(::onTouch)
        touchManager.addTouchReleaseListener(::onTouchUp)
    }

    private fun onSingleTap(touchInfo: TouchManager.TouchInfo) {
        if (touchInfo is TouchManager.TouchInfo.FilledInfo) {
            if (touchInfo.touchable is Selector) {
                processSelector(touchInfo.touchable)
            } else if (touchInfo.touchable is Selectable) {
                processSelectable(touchInfo.touchable)
            }
        } else {
            currentSelectable?.let { selectable ->
                val selector = selectable.getSelector()
                if (selector.isSelected()) {
                    selector.deselect()
                    touchManager.deleteTouchable(selector)
                    currentSelectable = null
                }
            }
        }
    }

    private fun onTouch(touchInfo: TouchManager.TouchInfo) {
        if (touchInfo is TouchManager.TouchInfo.FilledInfo) {
            if (touchInfo.touchable is Selector) {
                touchManager.addMoveListener(touchInfo.touchable::move)
            }
        }
    }

    private fun onTouchUp(touchInfo: TouchManager.TouchInfo) {
        if (touchInfo is TouchManager.TouchInfo.FilledInfo) {
            if (touchInfo.touchable is Selector) {
                touchManager.deleteMoveListener(touchInfo.touchable::move)
            }
        }
    }

    private fun processSelectable(selectable: Selectable) {
        val selector = selectable.getSelector()
        if (selector.isSelected()) {
            selector.deselect()
            currentSelectable = null
            touchManager.deleteTouchable(selector)
        } else {
            currentSelectable?.getSelector()?.let { currentSelector ->
                currentSelector.deselect()
                touchManager.deleteTouchable(currentSelector)
            }
            selector.select()
            currentSelectable = selectable
            touchManager.addTouchable(selector)
        }
    }

    private fun processSelector(selector: Selector) {
        touchManager.addMoveListener(selector::move)
    }
}