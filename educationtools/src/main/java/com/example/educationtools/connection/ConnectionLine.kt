package com.example.educationtools.connection

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

class ConnectionLine(val startKnot: Knot, val endKnot: Knot, gridSize: Float): ConnectionLineBase(gridSize) {

    init {
        updatePath(PointF(startKnot.getX(), startKnot.getY()), PointF(endKnot.getX(), endKnot.getY()), startKnot, endKnot)
        startKnot.addOnPositionChangedListener { x, y ->
            updatePath(PointF(x, y), PointF(endKnot.getX(), endKnot.getY()), startKnot, endKnot)
        }

        endKnot.addOnPositionChangedListener { x, y ->
            updatePath(PointF(startKnot.getX(), startKnot.getY()), PointF(x, y), startKnot, endKnot)
        }
    }

    private val linePaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        strokeWidth = 6f
        style = Paint.Style.STROKE
    }

    fun draw(canvas: Canvas) {
        canvas.apply {
            drawPath(path, linePaint)
        }
    }


    fun getConfigurations(): Configurations {
        return Configurations(startKnot.id, startKnot.logicBlockView.logicBlock.id, endKnot.id, endKnot.logicBlockView.logicBlock.id)
    }

    @JsonClass(generateAdapter = true)
    class Configurations(
        @Json(name = "start_knot_id")
        val startKnotId: String,
        @Json(name = "start_block_id")
        val startBlockId: String,
        @Json(name = "end_knot_id")
        val endKnotId: String,
        @Json(name = "end_block_id")
        val endBlockId: String
    )
}