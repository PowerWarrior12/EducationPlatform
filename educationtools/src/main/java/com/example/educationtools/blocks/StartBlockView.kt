package com.example.educationtools.blocks

import android.graphics.*
import androidx.core.graphics.toRegion
import com.example.educationtools.base.EditableBlockFactory
import com.example.educationtools.base.EditorViewBase
import com.example.educationtools.connection.Knot
import com.example.educationtools.logic.LogicBlock
import com.example.educationtools.logic.StartBlock
import com.example.educationtools.logic.parsers.InputVariablesParser
import com.example.educationtools.logic.parsers.StartBlockParser
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

class StartBlockView(private val startBlock: StartBlock = StartBlock()) : LogicBlockView() {

    private val parametersParser = StartBlockParser()
    private val inputVariableParser = InputVariablesParser()
    private var outputKnot = Knot(this, Knot.Side.BOTTOM, true, 20f, onKnotConnected = ::connect, onKnotDisconnected = ::disconnect)

    override val logicBlock: LogicBlock
        get() = startBlock
    override val inputKnots: List<Knot>
        get() = listOf()
    override val outputKnots: List<Knot>
        get() = listOf(outputKnot)

    //Paints
    private val mainPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    private val fillPaint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val mainPath = Path().apply {
        addOval(mainRect, Path.Direction.CCW)
    }

    private var mainRegion = Region().apply {
        setPath(mainPath, mainRect.toRegion())
    }

    override fun drawBorder(canvas: Canvas) {
        canvas.apply {
            drawPath(mainPath, fillPaint)
            drawPath(mainPath, mainPaint)
        }
    }

    override val configuration: EditableBlockFactory
        get() = Configurations(getCenter().x, getCenter().y, getWidth(), getHeight(), getText(), outputKnot.getConfig(), startBlock.id)

    override fun checkError() {

    }

    override fun checkPointAvailability(x: Float, y: Float): Boolean {
        return mainRegion.contains(x.toInt(), y.toInt())
    }

    override fun checkPointAvailability(point: PointF): Boolean {
        return checkPointAvailability(point.x, point.y)
    }

    override fun checkAndTouch(x: Float, y: Float) {
    }

    override fun updatePosition(newPosition: PointF) {
        super.updatePosition(newPosition)
        updateParams()
    }

    override fun updateSize(newWidth: Float, newHeight: Float) {
        super.updateSize(newWidth, newHeight)
        updateParams()
    }

    override fun setText(newText: String) {
        super.setText(newText)
        if (newText != "") {
            try {
                val variables = parametersParser.parseOrThrow(newText)
                startBlock.updateVariables(variables)
                isError = false
            } catch (e: java.lang.Exception) {
                startBlock.updateVariables(emptyList())
                isError = true
            }
        }
        onTextChangeListeners?.invoke(logicBlock.id)
        invalidate()
    }

    fun start(text: String) {
        val variables = inputVariableParser.parseOrThrow(text)
        startBlock.startOrThrow(variables)
    }

    private fun updateParams() {
        mainPath.apply {
            reset()
            addOval(mainRect, Path.Direction.CCW)
        }
        mainRegion = Region().apply {
            setPath(mainPath, mainRect.toRegion())
        }
        updateKnotsPosition()
    }

    private fun connect(knot: Knot) {
        startBlock.setNextBlock(knot.logicBlockView.logicBlock)
    }

    private fun disconnect(knot: Knot) {
        startBlock.deleteNextBlock()
    }

    private fun updateKnotsPosition() {
        outputKnot.updatePosition(mainRect.centerX(), mainRect.bottom)
    }

    @JsonClass(generateAdapter = true)
    data class Configurations(
        @Json(name = "center_x")
        var centerX: Float = 0f,
        @Json(name = "center_y")
        var centerY: Float = 0f,
        @Json(name = "width")
        var width: Float = 400f,
        @Json(name = "height")
        var height: Float = 250f,
        @Json(name = "text")
        var text: String = "",
        @Json(name = "output_knot")
        val outputKnot: Knot.Configurations,
        @Json(name = "id")
        val id: String
    ) : EditableBlockFactory {
        override val type: BlockType
            get() = BlockType.StartType

        override fun create(editor: EditorViewBase): StartBlockView {
            return StartBlockView(StartBlock(id)).apply {
                setEditorParent(editor)
                this.outputKnot = this@Configurations.outputKnot.generate(this, Knot.Side.BOTTOM, true, 20f, onKnotConnected = ::connect, onKnotDisconnected = ::disconnect)
                updatePosition(PointF(centerX, centerY))
                updateSize(width, height)
                setText(text)
            }
        }
    }
}