package com.example.educationtools.blocks

import android.graphics.Canvas
import com.example.educationtools.base.EditableBlockBase
import com.example.educationtools.logic.LogicBlock
import com.example.educationtools.logic.MemoryModel

abstract class LogicBlockView: EditableBlockBase() {
    abstract var logicBlock: LogicBlock
    abstract var inputKnots: List<Knot>
    abstract var outputKnots: List<Knot>

    abstract fun drawBorder(canvas: Canvas)

    private var _memoryModel: MemoryModel? = null

    val memoryModel: MemoryModel
        get() = checkNotNull(_memoryModel)

    private var isOutputConnectionProcess = false
    private var isInputConnectionProcess = false

    override fun drawFigure(canvas: Canvas) {
        drawBorder(canvas)
        if (getSelector().isSelected() || isOutputConnectionProcess) {
            outputKnots.forEach {
                it.draw(canvas)
            }
        }
        if (isInputConnectionProcess) {
            inputKnots.forEach {
                it.draw(canvas)
            }
        }
    }


    fun setMemoryModel(memoryModel: MemoryModel) {
        _memoryModel = memoryModel
        logicBlock.setMemoryModel(memoryModel)
    }

    fun changeInputConnectionStatus() {
        isInputConnectionProcess = !isInputConnectionProcess
    }

    fun changeOutputConnectionStatus() {
        isOutputConnectionProcess = !isOutputConnectionProcess
    }
}