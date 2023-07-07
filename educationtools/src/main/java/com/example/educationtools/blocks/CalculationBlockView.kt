package com.example.educationtools.blocks

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import com.example.educationtools.base.EditableBlockFactory
import com.example.educationtools.base.EditorViewBase
import com.example.educationtools.connection.Knot
import com.example.educationtools.logic.CalculationBlock
import com.example.educationtools.logic.LogicBlock
import com.example.educationtools.logic.parsers.CalculationBlockParser
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

class CalculationBlockView(private val calculationBlock: CalculationBlock = CalculationBlock()) : LogicBlockView() {

    private val parser = CalculationBlockParser(calculationBlock.id)
    private var leftKnot = Knot(this, Knot.Side.LEFT, false, 20f)
    private var rightKnot = Knot(this, Knot.Side.RIGHT, false, 20f)
    private var topKnot = Knot(this, Knot.Side.TOP, false, 20f)
    private var bottomKnot = Knot(this, Knot.Side.BOTTOM, true, 20f, onKnotConnected = ::connect, onKnotDisconnected = ::disconnect)


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

    override val logicBlock: LogicBlock
        get() = calculationBlock
    override val inputKnots: List<Knot>
        get() = listOf(leftKnot, rightKnot, topKnot)
    override val outputKnots: List<Knot>
        get() = listOf(bottomKnot)

    override fun drawBorder(canvas: Canvas) {
        canvas.apply {
            drawRect(mainRect, fillPaint)
            drawRect(mainRect, mainPaint)
        }
    }

    override fun checkError() {
        checkTextError()
    }

    override fun updatePosition(newPosition: PointF) {
        super.updatePosition(newPosition)
        updateKnotsPosition()
    }

    override fun updateSize(newWidth: Float, newHeight: Float) {
        super.updateSize(newWidth, newHeight)
        updateKnotsPosition()
    }

    override fun checkPointAvailability(x: Float, y: Float): Boolean {
        return mainRect.contains(x, y)
    }

    override fun checkPointAvailability(point: PointF): Boolean {
        return checkPointAvailability(point.x, point.y)
    }

    override fun checkAndTouch(x: Float, y: Float) {
    }

    private fun connect(knot: Knot) {
        calculationBlock.setNextBlock(knot.logicBlockView.logicBlock)
    }

    private fun disconnect(knot: Knot) {
        calculationBlock.deleteNextBlock()
    }

    private fun updateKnotsPosition() {
        leftKnot.updatePosition(mainRect.left, mainRect.centerY())
        rightKnot.updatePosition(mainRect.right, mainRect.centerY())
        topKnot.updatePosition(mainRect.centerX(), mainRect.top)
        bottomKnot.updatePosition(mainRect.centerX(), mainRect.bottom)
    }

    private fun checkTextError() {
        if (getText() != "") {
            isError = try {
                val (variable, function) = parser.parseOrThrow(getText(), memoryModel)
                calculationBlock.setFunctionAndVar(function, variable.name)
                false
            } catch (e: java.lang.Exception) {
                calculationBlock.deleteFunctionAndVar()
                true
            }
        }
    }

    override fun setText(newText: String) {
        super.setText(newText)
        checkTextError()
        onTextChangeListeners?.invoke(logicBlock.id)
        invalidate()
    }

    override val configuration: EditableBlockFactory
        get() = Configurations(
            getCenter().x,
            getCenter().y,
            getWidth(),
            getHeight(),
            getText(),
            leftKnot.getConfig(),
            rightKnot.getConfig(),
            topKnot.getConfig(),
            bottomKnot.getConfig(),
            logicBlock.id
        )

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
        @Json(name = "left_knot")
        val leftKnot: Knot.Configurations,
        @Json(name = "right_knot")
        val rightKnot: Knot.Configurations,
        @Json(name = "top_knot")
        val topKnot: Knot.Configurations,
        @Json(name = "bottom_knot")
        val bottomKnot: Knot.Configurations,
        @Json(name = "id")
        val id: String
    ) : EditableBlockFactory {
        override val type: BlockType
            get() = BlockType.CalculationType

        override fun create(editor: EditorViewBase): CalculationBlockView {
            return CalculationBlockView(CalculationBlock(id)).apply {
                setEditorParent(editor)
                this.leftKnot = this@Configurations.leftKnot.generate(this, Knot.Side.LEFT, false, 20f)
                this.rightKnot = this@Configurations.rightKnot.generate(this, Knot.Side.RIGHT, false, 20f)
                this.topKnot = this@Configurations.topKnot.generate(this, Knot.Side.TOP, false, 20f)
                this.bottomKnot = this@Configurations.bottomKnot.generate(
                    this,
                    Knot.Side.BOTTOM,
                    true,
                    20f,
                    onKnotConnected = ::connect,
                    onKnotDisconnected = ::disconnect
                )
                updatePosition(PointF(centerX, centerY))
                updateSize(width, height)
                setText(text)
            }
        }
    }
}