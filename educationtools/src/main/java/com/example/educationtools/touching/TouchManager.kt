package com.example.educationtools.touching

import android.util.Log
import com.example.educationtools.utils.Transformations

class TouchManager(
    private val transformations: Transformations
) {
    private val touchableSet = mutableSetOf<Touchable>()
    private var touchableInFocus: Touchable? = null
    private var startTouchableInFocus: Touchable? = null
    private val onTouchListeners = mutableListOf<(TouchInfo) -> Unit>()
    private val onSingleTouchListeners = mutableListOf<(TouchInfo) -> Unit>()
    private val onLongTouchListeners = mutableListOf<(TouchInfo) -> Unit>()
    private val onMoveListeners = mutableListOf<(TouchInfo) -> Unit>()
    private val onTouchReleaseListeners = mutableListOf<(TouchInfo) -> Unit>()
    private val onDoubleTouchListeners = mutableListOf<(TouchInfo) -> Unit>()

    private var longProcess: Boolean = false
    private var moveProcess: Boolean = false
    private var isReleased: Boolean = false

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

    fun addDoubleTouchListener(listener: (TouchInfo) -> Unit) {
        onDoubleTouchListeners.add(listener)
    }

    fun deleteSingleTouchListener(listener: (TouchInfo) -> Unit) {
        onSingleTouchListeners.remove(listener)
    }

    fun deleteLongTouchListener(listener: (TouchInfo) -> Unit) {
        onLongTouchListeners.remove(listener)
    }

    fun deleteMoveListener(listener: (TouchInfo) -> Unit) {
        onMoveListeners.remove(listener)
    }

    fun deleteTouchReleaseListener(listener: (TouchInfo) -> Unit) {
        onTouchReleaseListeners.remove(listener)
    }

    fun deleteDoubleTouchListener(listener: (TouchInfo) -> Unit) {
        onDoubleTouchListeners.remove(listener)
    }

    fun clear() {
        touchableSet.clear()
        touchableInFocus = null
        startTouchableInFocus = null
        longProcess = false
        moveProcess = false
        isReleased = false
    }

    fun touchDown(x: Float, y: Float) {
        isReleased = false
        Log.d("TOUCH-MANAGER", "touch down")
        val transformPoint = transformations.convertPointToTransform(x, y)
        touchableSet.firstOrNull { touchable ->
            touchable.checkPointAvailability(transformPoint)
        }?.let { touchable ->
            touchableInFocus = touchable
            startTouchableInFocus = touchable
            onTouchListeners.forEach { listener ->
                listener(TouchInfo.FilledInfo(transformPoint.x, transformPoint.y, startTouchableInFocus, touchable))
            }
            return
        }
        onTouchListeners.forEach { listener ->
            listener(TouchInfo.EmptyInfo(transformPoint.x, transformPoint.y, startTouchableInFocus))
        }
    }

    fun singleTouch(x: Float, y: Float) {

        Log.d("TOUCH-MANAGER", "single touch")
        val transformPoint = transformations.convertPointToTransform(x, y)
        touchableInFocus?.let { touchable ->
            onSingleTouchListeners.forEach { listener ->
                listener(TouchInfo.FilledInfo(transformPoint.x, transformPoint.y, startTouchableInFocus, touchable))
            }
            touchableInFocus = null
            startTouchableInFocus = null
            return
        }
        onSingleTouchListeners.forEach { listener ->
            listener(TouchInfo.EmptyInfo(transformPoint.x, transformPoint.y, startTouchableInFocus))
        }
    }

    fun longTouch(x: Float, y: Float) {

        Log.d("TOUCH-MANAGER", "long touch")
        val transformPoint = transformations.convertPointToTransform(x, y)
        longProcess = true
        touchableInFocus?.let { touchable ->
            onLongTouchListeners.forEach { listener ->
                listener(TouchInfo.FilledInfo(transformPoint.x, transformPoint.y, startTouchableInFocus, touchable))
            }
            return
        }
        onLongTouchListeners.forEach { listener ->
            listener(TouchInfo.EmptyInfo(transformPoint.x, transformPoint.y, startTouchableInFocus))
        }
    }

    fun move(x: Float, y: Float) {

        Log.d("TOUCH-MANAGER", "move touch")
        val transformPoint = transformations.convertPointToTransform(x, y)
        moveProcess = true
        touchableSet.firstOrNull { touchable ->
            touchable.checkPointAvailability(transformPoint)
        }?.let { touchable ->
            touchableInFocus = touchable
            onMoveListeners.forEach { listener ->
                listener(TouchInfo.FilledInfo(transformPoint.x, transformPoint.y, startTouchableInFocus, touchable))
            }
            return
        }
        touchableInFocus = null
        onMoveListeners.forEach { listener ->
            listener(TouchInfo.EmptyInfo(transformPoint.x, transformPoint.y, startTouchableInFocus))
        }
    }

    fun releaseTouch(x: Float, y: Float) {
        Log.d("TOUCH-MANAGER", "release touch")
        val transformPoint = transformations.convertPointToTransform(x, y)
        touchableInFocus?.let { touchable ->
            onTouchReleaseListeners.forEach { listener ->
                listener(TouchInfo.FilledInfo(transformPoint.x, transformPoint.y, startTouchableInFocus, touchable))
            }
            if (longProcess) {
                touchableInFocus = null
                startTouchableInFocus = null
                longProcess = false
            }
            if (moveProcess) {
                touchableInFocus = null
                startTouchableInFocus = null
                moveProcess = false
            }
            return
        }
        onTouchReleaseListeners.forEach { listener ->
            listener(TouchInfo.EmptyInfo(transformPoint.x, transformPoint.y, startTouchableInFocus))
        }
        startTouchableInFocus?.let {
            if (longProcess) {
                startTouchableInFocus = null
                longProcess = false
            }
            if (moveProcess) {
                startTouchableInFocus = null
                moveProcess = false
            }
        }
    }

    fun doubleTouch(x: Float, y: Float) {
        Log.d("TOUCH-MANAGER", "double touch")
        val transformPoint = transformations.convertPointToTransform(x, y)
        touchableSet.firstOrNull { touchable ->
            touchable.checkPointAvailability(transformPoint)
        }?.let { touchable ->
            onDoubleTouchListeners.forEach { listener ->
                listener(TouchInfo.FilledInfo(transformPoint.x, transformPoint.y, startTouchableInFocus, touchable))
            }
            return
        }
        onDoubleTouchListeners.forEach { listener ->
            listener(TouchInfo.EmptyInfo(transformPoint.x, transformPoint.y, startTouchableInFocus))
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

    fun isProcess(): Boolean {
        if (startTouchableInFocus != null) {
            return startTouchableInFocus!!.blockScroll
        }
        return false
    }

    fun onTwoPointTap(x: Float, y: Float) {
        if (!isReleased) {
            releaseTouch(x, y)
            isReleased = true
        }
    }

    sealed class TouchInfo(
        val xPos: Float,
        val yPos: Float,
        val startTouchable: Touchable?
    ) {
        class FilledInfo(xPos: Float, yPos: Float, startTouchable: Touchable?, val touchable: Touchable) :
            TouchInfo(xPos, yPos, startTouchable)

        class EmptyInfo(xPos: Float, yPos: Float, startTouchable: Touchable?) : TouchInfo(xPos, yPos, startTouchable)
    }
}