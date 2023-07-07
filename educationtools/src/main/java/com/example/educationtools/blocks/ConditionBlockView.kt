package com.example.educationtools.blocks

import android.graphics.*
import android.text.TextPaint
import androidx.core.graphics.toRegion
import com.example.educationtools.base.EditableBlockFactory
import com.example.educationtools.base.EditorViewBase
import com.example.educationtools.connection.Knot
import com.example.educationtools.logic.ConditionBlock
import com.example.educationtools.logic.LogicBlock
import com.example.educationtools.logic.parsers.ConditionBlockParser
import com.example.educationtools.utils.drawBlockText
import com.example.educationtools.utils.drawTextAboutSide
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.w3c.dom.Text

const val TRUE_COLOR = Color.GREEN
const val FALSE_COLOR = Color.RED
const val DIRECTIONS_TEXT_OFFSET = 20f
const val DIRECTION_TEXT_SIZE = 25f

class ConditionBlockView(private val conditionBlock: ConditionBlock = ConditionBlock()) :
    LogicBlockView() {

    private val parser = ConditionBlockParser(conditionBlock.id)
    private var falseKnot =
        Knot(this, Knot.Side.LEFT, true, 20f, false, ::falseConnect, ::falseDisconnect)
    private var trueKnot =
        Knot(this, Knot.Side.RIGHT, true, 20f, true, ::trueConnect, ::trueDisconnect)
    private var topKnot = Knot(this, Knot.Side.TOP, false, 20f)
    private var bottomKnot = Knot(this, Knot.Side.BOTTOM, false, 20f)

    override val logicBlock: LogicBlock
        get() = conditionBlock
    override val inputKnots: List<Knot>
        get() = listOf(topKnot, bottomKnot)
    override val outputKnots: List<Knot>
        get() = listOf(falseKnot, trueKnot)

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

    private val directionTextPaint = TextPaint().apply {
        isAntiAlias = true
        textSize = DIRECTION_TEXT_SIZE
    }

    private val mainPath = Path().apply {
        moveTo(mainRect.centerX(), mainRect.top)
        lineTo(mainRect.right, mainRect.centerY())
        lineTo(mainRect.centerX(), mainRect.bottom)
        lineTo(mainRect.left, mainRect.centerY())
        close()
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

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        drawDirectionsText(canvas)
    }

    private fun drawDirectionsText(canvas: Canvas) {
        canvas.drawTextAboutSide(
            "FALSE",
            directionTextPaint.apply {
                color = FALSE_COLOR
            },
            right = mainRect.left - DIRECTIONS_TEXT_OFFSET,
            bottom = mainRect.centerY() - DIRECTIONS_TEXT_OFFSET
        )
        canvas.drawTextAboutSide(
            "TRUE",
            directionTextPaint.apply {
                color = TRUE_COLOR
            },
            left = mainRect.right + DIRECTIONS_TEXT_OFFSET,
            bottom = mainRect.centerY() - DIRECTIONS_TEXT_OFFSET
        )
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
        updateProperties()
    }

    override fun updateSize(newWidth: Float, newHeight: Float) {
        super.updateSize(newWidth, newHeight)
        updateProperties()
    }

    override fun setText(newText: String) {
        super.setText(newText)
        checkTextError()
        onTextChangeListeners?.invoke(logicBlock.id)
        invalidate()
    }

    private fun checkTextError() {
        if (getText() != "") {
            isError = try {
                val function = parser.parseOrThrow(getText(), memoryModel)
                conditionBlock.setFunction(function)
                false
            } catch (e: java.lang.Exception) {
                conditionBlock.deleteFunction()
                true
            }
        }
    }

    private fun trueConnect(knot: Knot) {
        conditionBlock.setTrueBlock(knot.logicBlockView.logicBlock)
    }

    private fun falseConnect(knot: Knot) {
        conditionBlock.setFalseBlock(knot.logicBlockView.logicBlock)
    }

    private fun trueDisconnect(knot: Knot) {
        conditionBlock.deleteTrueBlock()
    }

    private fun falseDisconnect(knot: Knot) {
        conditionBlock.deleteFalseBlock()
    }

    private fun updateProperties() {
        mainPath.apply {
            reset()
            moveTo(mainRect.centerX(), mainRect.top)
            lineTo(mainRect.right, mainRect.centerY())
            lineTo(mainRect.centerX(), mainRect.bottom)
            lineTo(mainRect.left, mainRect.centerY())
            close()
        }

        mainRegion = Region().apply {
            setPath(mainPath, mainRect.toRegion())
        }

        falseKnot.updatePosition(mainRect.left, mainRect.centerY())
        trueKnot.updatePosition(mainRect.right, mainRect.centerY())
        topKnot.updatePosition(mainRect.centerX(), mainRect.top)
        bottomKnot.updatePosition(mainRect.centerX(), mainRect.bottom)
    }

    override val configuration: EditableBlockFactory
        get() = Configurations(
            getCenter().x,
            getCenter().y,
            getWidth(),
            getHeight(),
            getText(),
            falseKnot.getConfig(),
            trueKnot.getConfig(),
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
        @Json(name = "false_knot")
        val falseKnot: Knot.Configurations,
        @Json(name = "true_knot")
        val trueKnot: Knot.Configurations,
        @Json(name = "top_knot")
        val topKnot: Knot.Configurations,
        @Json(name = "bottom_knot")
        val bottomKnot: Knot.Configurations,
        @Json(name = "id")
        val id: String
    ) : EditableBlockFactory {
        override val type: BlockType
            get() = BlockType.ConditionType

        override fun create(editor: EditorViewBase): ConditionBlockView {
            return ConditionBlockView(ConditionBlock(id)).apply {
                setEditorParent(editor)
                this.falseKnot =
                    this@Configurations.falseKnot.generate(
                        this,
                        Knot.Side.LEFT,
                        true,
                        20f,
                        false,
                        ::falseConnect,
                        ::falseDisconnect
                    )
                this.trueKnot =
                    this@Configurations.trueKnot.generate(
                        this,
                        Knot.Side.RIGHT,
                        true,
                        20f,
                        true,
                        ::trueConnect,
                        ::trueDisconnect
                    )
                this.topKnot = this@Configurations.topKnot.generate(this, Knot.Side.TOP, false, 20f)
                this.bottomKnot =
                    this@Configurations.bottomKnot.generate(this, Knot.Side.BOTTOM, false, 20f)
                updatePosition(PointF(centerX, centerY))
                updateSize(width, height)
                setText(text)
            }
        }
    }
}