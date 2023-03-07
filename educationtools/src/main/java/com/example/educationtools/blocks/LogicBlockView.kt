package com.example.educationtools.blocks

import android.graphics.Canvas
import com.example.educationtools.base.EditableBlockBase
import com.example.educationtools.connection.Knot
import com.example.educationtools.logic.LogicBlock
import com.example.educationtools.logic.MemoryModel

abstract class LogicBlockView: EditableBlockBase() {

    private var _memoryModel: MemoryModel? = null
    private var isOutputProcess = false
    private var isInputProcess = false

    abstract val logicBlock: LogicBlock
    abstract val inputKnots: List<Knot>
    abstract val outputKnots: List<Knot>

    abstract fun drawBorder(canvas: Canvas)

    override fun drawFigure(canvas: Canvas) {
        drawBorder(canvas)
        if (getSelector().isSelected() || isOutputProcess) {
            outputKnots.forEach {
                it.draw(canvas)
            }
        }
        if (isInputProcess) {
            inputKnots.forEach {
                it.draw(canvas)
            }
        }
    }

    override fun getPriority(): Int {
        return 2
    }

    val memoryModel: MemoryModel
        get() = checkNotNull(_memoryModel)

    fun setMemoryModel(memoryModel: MemoryModel) {
        _memoryModel = memoryModel
        logicBlock.setMemoryModel(memoryModel)

    }

    fun changeOutputStatus() {
        isOutputProcess = !isOutputProcess
    }

    fun changeInputStatus() {
        isInputProcess = !isInputProcess
    }
}