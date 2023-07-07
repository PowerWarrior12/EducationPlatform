package com.example.educationtools.utils

import android.graphics.PointF
import android.util.Log

const val MAX_SCALE = 4f
const val MIN_SCALE = 0.25f

class Transformations(
    private var width: Float,
    private var height: Float
) {
    val translation = PointF(0f, 0f)

    var scale: Float = 1f
        private set

    fun updateSize(newWidth: Float = width, newHeight: Float = height) {
        width = newWidth
        height = newHeight
    }

    fun addTranslation(dx: Float, dy: Float) {
        translation.x = translation.x - dx / scale
        translation.y = translation.y - dy / scale
    }

    fun addScale(scale: Float) {
        this.scale *= scale
        this.scale = this.scale.coerceIn(MIN_SCALE, MAX_SCALE)
    }

    fun convertPointToTransform(x: Float, y: Float): PointF {
        return PointF().apply {
            this.x = (width / 2 - translation.x) + ((x - width / 2) / scale)
            this.y = (height / 2 - translation.y) + ((y - height / 2) / scale)
        }
    }

    fun convertPointToTransform(pointF: PointF): PointF {
        return convertPointToTransform(pointF.x, pointF.y)
    }
}