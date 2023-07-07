package com.example.educationtools.connection

import android.graphics.*
import androidx.core.graphics.toRegion
import com.example.educationtools.blocks.LogicBlockView
import com.example.educationtools.touching.Touchable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

class Knot(
    val logicBlockView: LogicBlockView,
    val side: Side,
    private val isOutput: Boolean,
    private val radius: Float,
    val direction: Boolean? = null,
    val onKnotConnected: ((Knot) -> Unit)? = null,
    val onKnotDisconnected: ((Knot) -> Unit)? = null,
    val id: String = UUID.randomUUID().toString()
) : Touchable {

    private var xPos: Float = 0f

    private var yPos: Float = 0f

    private val pathMain = Path().apply {
        addCircle(xPos, yPos, radius, Path.Direction.CCW)
    }

    private var isFocus = false

    private val rectTouch = RectF().apply {
        left = xPos - radius * 3
        right = xPos + radius * 3
        top = yPos - radius * 3
        bottom = yPos + radius * 3
    }

    private val pathCore = Path().apply {
        addCircle(xPos, yPos, radius / 2f, Path.Direction.CCW)
    }

    private val pathTouch = Path().apply {
        addCircle(xPos, yPos, radius * 3, Path.Direction.CCW)
    }

    private var mainRegion = Region().apply {
        setPath(pathTouch, rectTouch.toRegion())
    }

    private val fillPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    private val focusPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.GREEN
        alpha = 100
    }

    private val borderPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 3f
    }

    private val fillCorePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.BLACK
    }

    private val onPositionChangedListeners: MutableList<(Float, Float) -> Unit> = mutableListOf()

    override fun checkPointAvailability(x: Float, y: Float): Boolean {
        return mainRegion.contains(x.toInt(), y.toInt())
    }

    override fun checkPointAvailability(point: PointF): Boolean {
        return checkPointAvailability(point.x, point.y)
    }

    override fun checkAndTouch(x: Float, y: Float) {

    }

    override fun getPriority(): Int {
        return 1
    }

    override val blockScroll: Boolean
        get() = true

    fun updatePosition(newX: Float, newY: Float) {
        xPos = newX
        yPos = newY
        rectTouch.apply {
            left = xPos - radius * 3
            right = xPos + radius * 3
            top = yPos - radius * 3
            bottom = yPos + radius * 3
        }

        pathMain.apply {
            reset()
            addCircle(xPos, yPos, radius, Path.Direction.CCW)
        }

        pathTouch.apply {
            reset()
            addCircle(xPos, yPos, radius * 3, Path.Direction.CCW)
        }

        pathCore.apply {
            reset()
            addCircle(xPos, yPos, radius / 2f, Path.Direction.CCW)
        }

        mainRegion = Region().apply {
            setPath(pathTouch, rectTouch.toRegion())
        }
        onPositionChangedListeners.forEach { it(xPos, yPos) }
    }

    fun draw(canvas: Canvas) {
        canvas.apply {
            drawPath(pathMain, fillPaint)
            drawPath(pathMain, borderPaint)
            if (isOutput) {
                drawPath(pathCore, fillCorePaint)
            }
            if (isFocus) {
                drawPath(pathTouch, focusPaint)
            }
        }
    }

    fun addOnPositionChangedListener(listener: (Float, Float) -> Unit) {
        onPositionChangedListeners.add(listener)
    }

    fun getRadius(): Float {
        return radius
    }

    fun getX(): Float {
        return xPos
    }

    fun getY(): Float {
        return yPos
    }

    fun connectedWithKnot(knot: Knot) {
        onKnotConnected?.invoke(knot)
    }

    fun disconnectedWithKnot(knot: Knot) {
        onKnotDisconnected?.invoke(knot)
    }

    fun setFocus() {
        isFocus = true
    }

    fun deleteFocus() {
        isFocus = false
    }

    fun getConfig(): Configurations {
        return Configurations(id)
    }

    @JsonClass(generateAdapter = false)
    enum class Side {
        LEFT, RIGHT, TOP, BOTTOM
    }

    @JsonClass(generateAdapter = true)
    class Configurations(
        @Json(name = "id")
        val id: String
    ) {
        fun generate(
            logicBlockView: LogicBlockView,
            side: Side,
            isOutput: Boolean,
            radius: Float,
            direction: Boolean? = null,
            onKnotConnected: ((Knot) -> Unit)? = null,
            onKnotDisconnected: ((Knot) -> Unit)? = null
        ): Knot {
            return Knot(logicBlockView, side, isOutput, radius, direction, onKnotConnected, onKnotDisconnected, id)
        }
    }
}