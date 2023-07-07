package com.example.educationtools.blocks

import android.graphics.*
import androidx.core.graphics.toRegion
import com.example.educationtools.base.EditableBlockFactory
import com.example.educationtools.base.EditorViewBase
import com.example.educationtools.connection.Knot
import com.example.educationtools.logic.EndBlock
import com.example.educationtools.logic.LogicBlock
import com.example.educationtools.logic.Variable
import com.example.educationtools.logic.parsers.EndBlockParser
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

class EndBlockView(private val endBlock: EndBlock = EndBlock()): LogicBlockView() {

    private val parser = EndBlockParser(endBlock.id)
    private var inputKnot = Knot(this, Knot.Side.TOP, false, 20f)

    override val logicBlock: LogicBlock
        get() = endBlock
    override val inputKnots: List<Knot>
        get() = listOf(inputKnot)
    override val outputKnots: List<Knot>
        get() = listOf()

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

    override fun checkError() {
        checkTextError()
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
        checkTextError()
        invalidate()
    }

    fun addOnSuccessListener(listener: (List<Variable>) -> Unit) {
        endBlock.addOnEndListener(listener)
    }

    private fun checkTextError() {
        if (getText() != "") {
            try {
                val variables = parser.parseOrThrow(getText(), memoryModel)
                endBlock.setVariables(variables)
                isError = false
            } catch (e: java.lang.Exception) {
                endBlock.setVariables(emptyList())
                isError = true
            }
        }
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

    private fun updateKnotsPosition() {
        inputKnot.updatePosition(mainRect.centerX(), mainRect.top)
    }

    override val configuration: EditableBlockFactory
        get() = Configurations(getCenter().x, getCenter().y, getWidth(), getHeight(), getText(), inputKnot.getConfig(), logicBlock.id)
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
        @Json(name = "input_knot")
        val inputKnot: Knot.Configurations,
        @Json(name = "id")
        val id: String
    ) : EditableBlockFactory {
        override val type: BlockType
            get() = BlockType.EndType

        override fun create(editor: EditorViewBase): EndBlockView {
            return EndBlockView(EndBlock(id)).apply {
                setEditorParent(editor)
                this.inputKnot = this@Configurations.inputKnot.generate(this, Knot.Side.TOP, false, 20f)
                updatePosition(PointF(centerX, centerY))
                updateSize(width, height)
                setText(text)
            }
        }
    }
}