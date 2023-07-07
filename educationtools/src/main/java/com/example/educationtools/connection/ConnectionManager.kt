package com.example.educationtools.connection

import android.graphics.Canvas
import com.example.educationtools.base.ParentEditor
import com.example.educationtools.blocks.LogicBlockView
import com.example.educationtools.logic.MemoryModel
import com.example.educationtools.selection.SelectManager
import com.example.educationtools.touching.TouchManager

class ConnectionManager(
    private val memoryModel: MemoryModel,
    private val touchManager: TouchManager,
    private val parentEditor: ParentEditor,
    private val selectorManager: SelectManager
) {
    private val logicBlocksView = mutableListOf<LogicBlockView>()
    private var connectionLineShadow: ConnectionLineShadow? = null
    private var connectableList = mutableListOf<String>()
    private val connectionLines = mutableListOf<ConnectionLine>()

    private var onConnectionListener: ((start: String, end: String) -> Unit)? = null

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

    fun clear() {
        logicBlocksView.clear()
        connectionLineShadow = null
        connectableList.clear()
        connectionLines.clear()
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

    fun deleteLogicBlockView(logicBlockView: LogicBlockView) {
        logicBlocksView.remove(logicBlockView)
        logicBlockView.outputKnots.forEach { startKnot ->
            deleteKnotConnection(startKnot)
        }
        logicBlockView.inputKnots.forEach { endKnot ->
            deleteInputKnotConnection(endKnot)
        }
    }

    fun addOnConnectedListener(listener: (start: String, end: String) -> Unit) {
        onConnectionListener = listener
    }

    fun saveConnections(): List<ConnectionLine.Configurations> {
        return connectionLines.map {
            it.getConfigurations()
        }
    }

    fun loadConnections(list: List<ConnectionLine.Configurations>) {
        list.forEach { config ->
            val startBlock = logicBlocksView.firstOrNull {
                it.logicBlock.id == config.startBlockId
            } ?: return@forEach
            val endBlock = logicBlocksView.firstOrNull {
                it.logicBlock.id == config.endBlockId
            } ?: return@forEach

            val startKnot = startBlock.getKnotById(config.startKnotId) ?: return@forEach
            val endKnot = endBlock.getKnotById(config.endKnotId) ?: return@forEach

            startKnot.connectedWithKnot(endKnot)
            connectionLines.add(ConnectionLine(startKnot, endKnot, parentEditor.getGridSize()))
            onConnectionListener?.invoke(startKnot.logicBlockView.logicBlock.id, endKnot.logicBlockView.logicBlock.id)
        }
        parentEditor.invalidate()
    }

    private fun onTouch(touchInfo: TouchManager.TouchInfo) {
        if (touchInfo is TouchManager.TouchInfo.FilledInfo) {
            if (touchInfo.touchable is Knot) {
                connectableList.addAll(
                    memoryModel.getAvailableBlocksOrThrow(
                        touchInfo.touchable.logicBlockView.logicBlock.id,
                        touchInfo.touchable.direction
                    )
                )
                logicBlocksView.forEach { block ->
                    if (block.logicBlock.id in connectableList) {
                        block.changeInputStatus()
                        block.inputKnots.forEach { knot ->
                            touchManager.addTouchable(knot)
                        }
                    }
                }
                selectorManager.cancelSelection()
                connectionLineShadow = ConnectionLineShadow(touchInfo.touchable, parentEditor.getGridSize())
                connectionLineShadow?.updateTargetPoint(touchInfo.xPos, touchInfo.yPos)
                parentEditor.invalidate()
            }
        }
    }

    private fun onMove(touchInfo: TouchManager.TouchInfo) {
        if (connectionLineShadow != null) {
            if (touchInfo is TouchManager.TouchInfo.FilledInfo && touchInfo.touchable is Knot && touchInfo.touchable != connectionLineShadow!!.startKnot) {
                connectionLineShadow?.getFocusOrNull()?.deleteFocus()
                connectionLineShadow?.deleteFocus()
                connectionLineShadow?.setFocus(touchInfo.touchable)
                touchInfo.touchable.setFocus()
            } else {
                connectionLineShadow?.getFocusOrNull()?.deleteFocus()
                connectionLineShadow?.deleteFocus()
            }
            connectionLineShadow?.updateTargetPoint(touchInfo.xPos, touchInfo.yPos)
            parentEditor.invalidate()
        }
    }

    private fun onRelease(touchInfo: TouchManager.TouchInfo) {
        if (connectionLineShadow != null) {
            connectionLineShadow?.getFocusOrNull()?.deleteFocus()
            connectionLineShadow?.deleteFocus()
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

                    deleteKnotConnection(startKnot)

                    startKnot.connectedWithKnot(endKnot)
                    connectionLines.add(ConnectionLine(startKnot, endKnot, parentEditor.getGridSize()))
                    onConnectionListener?.invoke(startKnot.logicBlockView.logicBlock.id, endKnot.logicBlockView.logicBlock.id)
                }
            } else {
                deleteKnotConnection(connectionLineShadow!!.startKnot)
            }

            connectionLineShadow = null
            selectorManager.restoreSelectable()
            parentEditor.invalidate()
        }
    }

    private fun deleteKnotConnection(startKnot: Knot) {
        connectionLines.firstOrNull { connectionLine ->
            connectionLine.startKnot == startKnot
        }?.let { connectionLine ->
            connectionLine.startKnot.disconnectedWithKnot(connectionLine.endKnot)
            connectionLines.remove(connectionLine)
        }
    }

    private fun deleteInputKnotConnection(endKnot: Knot) {
        connectionLines.filter { connectionLine ->
            connectionLine.endKnot == endKnot
        }.forEach { connectionLine ->
            connectionLine.startKnot.disconnectedWithKnot(connectionLine.endKnot)
            connectionLines.remove(connectionLine)
        }
    }
}