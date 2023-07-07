package com.example.educationtools.utils

import android.animation.TypeEvaluator
import android.graphics.PointF

private class PointEvaluator(): TypeEvaluator<PointF> {
    override fun evaluate(p0: Float, p1: PointF?, p2: PointF?): PointF {
        val resultPoint = PointF()
        p1?.let {
            resultPoint.x = p1.x * p0
            resultPoint.y = p1.y * p0
        }
        return resultPoint
    }
}