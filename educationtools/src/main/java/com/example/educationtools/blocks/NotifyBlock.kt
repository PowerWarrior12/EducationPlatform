package com.example.educationtools.blocks

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import com.example.educationtools.base.EditableBlockBase
import com.example.educationtools.base.EditableBlockFactory
import com.example.educationtools.base.EditorViewBase
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

class NotifyBlock(): EditableBlockBase() {

    //Paints
    val mainPaint = Paint().apply {
        isAntiAlias = true
        color = Color.RED
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    override fun drawFigure(canvas: Canvas) {
        canvas.apply {
            drawRect(mainRect, mainPaint)
        }
    }

    override fun checkPointAvailability(x: Float, y: Float): Boolean {
        return mainRect.contains(x, y)
    }

    override fun checkPointAvailability(point: PointF): Boolean {
        return checkPointAvailability(point.x, point.y)
    }

    override fun checkAndTouch(x: Float, y: Float) {
    }

    override fun getPriority(): Int {
        return 2
    }

    override val configuration: EditableBlockFactory
        get() = Configurations(getCenter().x, getCenter().y, getWidth(), getHeight(), getText())

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
        var text: String = ""
    ) : EditableBlockFactory {
        override val type: BlockType
            get() = BlockType.NotificationType

        override fun create(editor: EditorViewBase): NotifyBlock {
            return NotifyBlock().apply {
                setEditorParent(editor)
                updatePosition(PointF(centerX, centerY))
                updateSize(width, height)
                setText(text)
            }
        }
    }

}