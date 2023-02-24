package com.example.educationtools.control

import android.view.MotionEvent
import com.example.educationtools.base.Touchable
import com.example.educationtools.utils.Transformations

class TouchManager(
    private val transformations: Transformations
) {
    private val touchableSet = mutableSetOf<Touchable>()
    private var touchableInFocus: Touchable? = null
    private val onTouchListeners = mutableListOf<(TouchInfo) -> Unit>()
    private val onSingleTouchListeners = mutableListOf<(TouchInfo) -> Unit>()
    private val onLongTouchListeners = mutableListOf<(TouchInfo) -> Unit>()
    private val onMoveListeners = mutableListOf<(TouchInfo) -> Unit>()
    private val onTouchReleaseListeners = mutableListOf<(TouchInfo) -> Unit>()

    private var longProcess: Boolean = false

    fun addTouchListener(listener: (TouchInfo) -> Unit) {
        onTouchListeners.add(listener)
    }

    fun addSingleTouchListener(listener: (TouchInfo) -> Unit) {
        onSingleTouchListeners.add(listener)
    }

    fun addLongTouchListener(listener: (TouchInfo) -> Unit) {
        onLongTouchListeners.add(listener)
    }

    fun addMoveListener(listener: (TouchInfo) -> Unit) {
        onMoveListeners.add(listener)
    }

    fun addTouchReleaseListener(listener: (TouchInfo) -> Unit) {
        onTouchReleaseListeners.add(listener)
    }

    fun touchDown(x: Float, y: Float) {
        val transformPoint = transformations.convertPointToTransform(x, y)
        touchableSet.firstOrNull { touchable ->
            touchable.checkPointAvailability(transformPoint)
        }?.let { touchable ->
            touchableInFocus = touchable
            onTouchListeners.forEach { listener ->
                listener(TouchInfo(touchable, transformPoint.x, transformPoint.y))
            }
        }
    }

    fun singleTouch(x: Float, y: Float) {
        val transformPoint = transformations.convertPointToTransform(x, y)
        touchableInFocus?.let { touchable ->
            onSingleTouchListeners.forEach { listener ->
                listener(TouchInfo(touchable, transformPoint.x, transformPoint.y))
            }
            touchableInFocus = null
        }
    }

    fun longTouch(x: Float, y: Float) {
        val transformPoint = transformations.convertPointToTransform(x, y)
        longProcess = true
        touchableInFocus?.let { touchable ->
            onLongTouchListeners.forEach { listener ->
                listener(TouchInfo(touchable, transformPoint.x, transformPoint.y))
            }
        }
    }

    fun move(x: Float, y: Float) {
        val transformPoint = transformations.convertPointToTransform(x, y)
        touchableInFocus?.let { touchable ->
            onMoveListeners.forEach { listener ->
                listener(TouchInfo(touchable, transformPoint.x, transformPoint.y))
            }
        }
    }

    fun releaseTouch(x: Float, y: Float) {
        val transformPoint = transformations.convertPointToTransform(x, y)
        touchableInFocus?.let { touchable ->
            onTouchReleaseListeners.forEach { listener ->
                listener(TouchInfo(touchable, transformPoint.x, transformPoint.y))
            }
        }
        if (longProcess) {
            touchableInFocus = null
            longProcess = false
        }
    }

    fun addTouchable(touchable: Touchable) {
        touchableSet.add(touchable)
        val sorted = touchableSet.sortedBy { currentTouchable ->
            currentTouchable.getPriority()
        }
        touchableSet.clear()
        touchableSet.addAll(sorted)
    }

    fun deleteTouchable(touchable: Touchable) {
        touchableSet.remove(touchable)
        val sorted = touchableSet.sortedBy { currentTouchable ->
            currentTouchable.getPriority()
        }
        touchableSet.clear()
        touchableSet.addAll(sorted)
    }

    data class TouchInfo(
        val touchable: Touchable,
        val xPos: Float,
        val yPos: Float
    )
}
