package com.example.educationtools.blocks

import android.graphics.Canvas
import android.graphics.Color
import android.text.Layout
import android.text.TextPaint
import com.example.educationtools.base.EditableBlockBase
import com.example.educationtools.base.ParentEditor
import com.example.educationtools.connection.Knot
import com.example.educationtools.logic.LogicBlock
import com.example.educationtools.logic.MemoryModel
import com.example.educationtools.utils.drawBlockText

abstract class LogicBlockView : EditableBlockBase() {

    private var _memoryModel: MemoryModel? = null
    private var isOutputProcess = false
    private var isInputProcess = false
    protected var onTextChangeListeners : ((String) -> Unit)? = null

    private val errorPaintText = TextPaint().apply {
        isAntiAlias = true
        isUnderlineText = true
        color = Color.RED
        textSize = textPaint.textSize
    }

    abstract val logicBlock: LogicBlock
    abstract val inputKnots: List<Knot>
    abstract val outputKnots: List<Knot>

    abstract fun drawBorder(canvas: Canvas)
    abstract fun checkError()

    fun getKnotById(knotId: String): Knot? {
        inputKnots.forEach {
            if (it.id == knotId) return it
        }
        outputKnots.forEach {
            if (it.id == knotId) return it
        }
        return null
    }

    override fun setEditorParent(parentEditor: ParentEditor) {
        super.setEditorParent(parentEditor)
        setMemoryModel(parentEditor.getMemoryModel())
    }

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

    override fun drawText(canvas: Canvas) {
        if (isError) {
            canvas.apply {
                drawBlockText(
                    getText(),
                    errorPaintText,
                    textRect.width().toInt(),
                    textRect,
                    horizontalAlignment = Layout.Alignment.ALIGN_CENTER,
                    verticalAlignment = Layout.Alignment.ALIGN_CENTER
                )
            }
        }
        super.drawText(canvas)
    }

    override fun getPriority(): Int {
        return 2
    }

    var isError: Boolean = false
        protected set

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

    fun setTextChangeListener(listener: (String) -> Unit) {
        onTextChangeListeners = listener
    }
}