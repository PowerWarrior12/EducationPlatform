package com.example.educationtools.connection

import android.graphics.Canvas
import com.example.educationtools.base.ParentEditor
import com.example.educationtools.blocks.Knot
import com.example.educationtools.blocks.LogicBlockView
import com.example.educationtools.logic.MemoryModel
import com.example.educationtools.touching.TouchManager

class ConnectionManager(private val memoryModel: MemoryModel, private val touchManager: TouchManager, private val parentEditor: ParentEditor) {
    private val logicBlocksView = mutableListOf<LogicBlockView>()
    private var connectionLineShadow: ConnectionLineShadow? = null
    private var connectableList = mutableListOf<String>()
    private val connectionLines = mutableListOf<ConnectionLine>()

    init {
        touchManager.addTouchListener(::onTouch)
        touchManager.addMoveListener(::onMove)
        touchManager.addTouchReleaseListener(::onRelease)
    }

    fun draw(canvas: Canvas) {
        connectionLineShadow?.draw(canvas)
        connectionLines.forEach {
            it.draw(canvas)
        }
    }

    fun addLogicBlockView(logicBlockView: LogicBlockView) {
        logicBlocksView.add(logicBlockView)
        logicBlockView.getSelector().addOnSelectListener {
            logicBlockView.changeOutputStatus()
            logicBlockView.outputKnots.forEach { knot ->
                touchManager.addTouchable(knot)
            }
        }
        logicBlockView.getSelector().addOnDeselectListener {
            logicBlockView.changeOutputStatus()
            logicBlockView.outputKnots.forEach { knot ->
                touchManager.deleteTouchable(knot)
            }
        }
    }

    private fun onTouch(touchInfo: TouchManager.TouchInfo) {
        if (touchInfo is TouchManager.TouchInfo.FilledInfo) {
            if (touchInfo.touchable is Knot) {
                connectableList.addAll(memoryModel.getAvailableBlocksOrThrow(touchInfo.touchable.logicBlockView.logicBlock.id, touchInfo.touchable.direction))
                logicBlocksView.forEach { block ->
                    if (block.logicBlock.id in connectableList) {
                        block.changeInputStatus()
                        block.inputKnots.forEach { knot ->
                            touchManager.addTouchable(knot)
                        }
                    }
                }
                connectionLineShadow = ConnectionLineShadow(touchInfo.touchable)
                connectionLineShadow?.updateTargetPoint(touchInfo.xPos, touchInfo.yPos)
                parentEditor.invalidate()
            }
        }
    }

    private fun onMove(touchInfo: TouchManager.TouchInfo) {
        if (connectionLineShadow != null) {
            connectionLineShadow?.updateTargetPoint(touchInfo.xPos, touchInfo.yPos)
            parentEditor.invalidate()
        }
    }
    private fun onRelease(touchInfo: TouchManager.TouchInfo) {
        if (connectionLineShadow != null) {
            logicBlocksView.forEach { block ->
                if (block.logicBlock.id in connectableList) {
                    block.changeInputStatus()
                    block.inputKnots.forEach { knot ->
                        touchManager.deleteTouchable(knot)
                    }
                }
            }

            connectableList.clear()

            if (touchInfo is TouchManager.TouchInfo.FilledInfo && touchInfo.touchable is Knot) {
                if (touchInfo.touchable != connectionLineShadow!!.startKnot) {
                    val startKnot = connectionLineShadow!!.startKnot
                    val endKnot = touchInfo.touchable
                    connectionLines.add(ConnectionLine(startKnot, endKnot))
                }
            }

            connectionLineShadow = null
            parentEditor.invalidate()
        }
    }
}