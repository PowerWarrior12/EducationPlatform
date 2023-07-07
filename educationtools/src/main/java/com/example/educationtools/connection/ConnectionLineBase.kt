package com.example.educationtools.connection

import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import com.example.educationtools.utils.extensions.coerceOut
import com.example.educationtools.utils.extensions.roundToValue
import java.lang.Float.min
import kotlin.math.max

private const val blockOffset = 80f
private const val arrowHeight = 40f
private const val arrowWidth = 60f
open class ConnectionLineBase(private val gridSize: Float) {

    private val pointStart = PointF()
    private val pointTwo = PointF()
    private val pointThree = PointF()
    private val pointFour = PointF()
    private val pointFive = PointF()
    private val pointSix = PointF()
    private val pointSeven = PointF()
    private val pointEnd = PointF()
    protected val path = Path()

    protected fun updatePath(startPoint: PointF, endPoint: PointF, startKnot: Knot, endKnot: Knot?) {

        val block = startKnot.logicBlockView
        val startBlockRect = RectF().apply {
            left = block.getCenter().x - block.getWidth() / 2
            right = block.getCenter().x + block.getWidth() / 2
            top = block.getCenter().y - block.getHeight() / 2
            bottom = block.getCenter().y + block.getHeight() / 2
        }

        pointStart.apply {
            x = startPoint.x
            y = startPoint.y
        }
        pointTwo.apply {
            when (startKnot.side) {
                Knot.Side.BOTTOM -> {
                    x = startPoint.x
                    y = (startPoint.y + (endPoint.y - startPoint.y) / 2).coerceAtLeast(startPoint.y + blockOffset)
                }
                Knot.Side.TOP -> {
                    x = startPoint.x
                    y = (startPoint.y - (startPoint.y - endPoint.y) / 2).coerceAtMost(startPoint.y - blockOffset)
                }
                Knot.Side.LEFT -> {
                    x = if (endPoint.y < startBlockRect.bottom && endPoint.y > startBlockRect.top) {
                        (startPoint.x - (startPoint.x - endPoint.x) / 2).coerceAtMost(startPoint.x - blockOffset)
                    } else {
                        endPoint.x.coerceAtMost(startPoint.x - blockOffset)
                    }
                    y = startPoint.y
                }
                Knot.Side.RIGHT -> {
                    x = if (endPoint.y < startBlockRect.bottom && endPoint.y > startBlockRect.top) {
                        (startPoint.x + (endPoint.x - startPoint.x) / 2).coerceAtLeast(startPoint.x + blockOffset)
                    } else {
                        endPoint.x.coerceAtLeast(startPoint.x + blockOffset)
                    }
                    y = startPoint.y
                }
            }
        }
        pointThree.apply {
            when (startKnot.side) {
                Knot.Side.BOTTOM -> {
                    x = if (endPoint.y < startBlockRect.top) {
                        endPoint.x.coerceOut(startBlockRect.left - blockOffset, startBlockRect.right + blockOffset)
                    } else if (endPoint.y > startBlockRect.bottom + blockOffset) {
                        endPoint.x
                    } else {
                        if (endPoint.x > startBlockRect.right) {
                            startBlockRect.right + (endPoint.x - startBlockRect.right) / 2
                        } else if (endPoint.x < startBlockRect.left) {
                            startBlockRect.left - (startBlockRect.left - endPoint.x) / 2
                        } else {
                            endPoint.x.coerceOut(startBlockRect.left - blockOffset, startBlockRect.right + blockOffset)
                        }
                    }
                    y = pointTwo.y
                }
                Knot.Side.TOP -> {
                    x = if (endPoint.y > startBlockRect.bottom) {
                        endPoint.x.coerceOut(startBlockRect.left - blockOffset, startBlockRect.right + blockOffset)
                    } else if (endPoint.y < startBlockRect.top) {
                        endPoint.x
                    } else {
                        if (endPoint.x > startBlockRect.right) {
                            startBlockRect.right + (endPoint.x - startBlockRect.right) / 2
                        } else if (endPoint.x < startBlockRect.left) {
                            startBlockRect.left - (startBlockRect.left - endPoint.x) / 2
                        } else {
                            endPoint.x.coerceOut(startBlockRect.left - blockOffset, startBlockRect.right + blockOffset)
                        }
                    }
                    y = pointTwo.y
                }
                Knot.Side.LEFT -> {
                    x = pointTwo.x
                    y = if (endPoint.x > startBlockRect.left - blockOffset) {
                        if (endPoint.y > startBlockRect.bottom) {
                            (startBlockRect.bottom + (endPoint.y - startBlockRect.bottom) / 2)
                        } else if (endPoint.y < startBlockRect.top) {
                            (startBlockRect.top - (startBlockRect.top - endPoint.y) / 2)
                        } else {
                            endPoint.y.coerceOut(startBlockRect.top - blockOffset, startBlockRect.bottom + blockOffset)
                        }
                    } else {
                        if (endPoint.y in startBlockRect.top..startBlockRect.bottom) {
                            endPoint.y
                        } else {
                            pointTwo.y
                        }
                    }
                }
                Knot.Side.RIGHT -> {
                    x = pointTwo.x
                    y = if (endPoint.x < startBlockRect.right + blockOffset) {
                        if (endPoint.y > startBlockRect.bottom) {
                            (startBlockRect.bottom + (endPoint.y - startBlockRect.bottom) / 2)
                        } else if (endPoint.y < startBlockRect.top) {
                            (startBlockRect.top - (startBlockRect.top - endPoint.y) / 2)
                        } else {
                            endPoint.y.coerceOut(startBlockRect.top - blockOffset, startBlockRect.bottom + blockOffset)
                        }
                    } else {
                        if (endPoint.y in startBlockRect.top..startBlockRect.bottom) {
                            endPoint.y
                        } else {
                            pointTwo.y
                        }
                    }
                }
            }
        }
        pointFour.apply {
            when (startKnot.side) {
                Knot.Side.BOTTOM -> {
                    x = pointThree.x
                    y = if (endPoint.y > startBlockRect.top) {
                        endPoint.y
                    } else {
                        (startBlockRect.top - (startBlockRect.top - endPoint.y) / 2).coerceAtMost(startBlockRect.top - blockOffset)
                    }
                }
                Knot.Side.TOP -> {
                    x = pointThree.x
                    y = if (endPoint.y < startBlockRect.bottom) {
                        endPoint.y
                    } else {
                        (endPoint.y + (endPoint.y - startBlockRect.bottom) / 2).coerceAtLeast(startBlockRect.bottom + blockOffset)
                    }
                }
                Knot.Side.RIGHT -> {
                    x =
                        if (endPoint.y in startBlockRect.top..startBlockRect.bottom && endPoint.x < startBlockRect.left) {
                            (startBlockRect.left - (startBlockRect.left - endPoint.x) / 2).coerceAtMost(startBlockRect.left - blockOffset)
                        } else {
                            endPoint.x
                        }
                    y = if (endPoint.x < startBlockRect.right + blockOffset) {
                        if (endPoint.y > startBlockRect.bottom) {
                            startBlockRect.bottom + (endPoint.y - startBlockRect.bottom) / 2
                        } else if (endPoint.y < startBlockRect.top) {
                            startBlockRect.top - (startBlockRect.top - endPoint.y) / 2
                        } else {
                            endPoint.y.coerceOut(startBlockRect.top - blockOffset, startBlockRect.bottom + blockOffset)
                        }
                    } else {
                        endPoint.y
                    }
                }
                Knot.Side.LEFT -> {
                    x =
                        if (endPoint.y in startBlockRect.top..startBlockRect.bottom && endPoint.x > startBlockRect.right) {
                            (startBlockRect.right + (endPoint.x - startBlockRect.right) / 2).coerceAtLeast(
                                startBlockRect.right + blockOffset
                            )
                        } else {
                            endPoint.x
                        }
                    y = if (endPoint.x > startBlockRect.left - blockOffset) {
                        if (endPoint.y > startBlockRect.bottom) {
                            startBlockRect.bottom + (endPoint.y - startBlockRect.bottom) / 2
                        } else if (endPoint.y < startBlockRect.top) {
                            startBlockRect.top - (startBlockRect.top - endPoint.y) / 2
                        } else {
                            endPoint.y.coerceOut(startBlockRect.top - blockOffset, startBlockRect.bottom + blockOffset)
                        }
                    } else {
                        endPoint.y
                    }
                }
            }
        }
        pointFive.apply {
            when (startKnot.side) {
                Knot.Side.BOTTOM -> {
                    x =
                        if (endPoint.y < startBlockRect.top && endPoint.x in startBlockRect.left - blockOffset..startBlockRect.right + blockOffset) {
                            endPoint.x
                        } else {
                            pointFour.x
                        }
                    y = pointFour.y
                }
                Knot.Side.TOP -> {
                    x =
                        if (endPoint.y > startBlockRect.bottom && endPoint.x in startBlockRect.left - blockOffset..startBlockRect.right + blockOffset) {
                            endPoint.x
                        } else {
                            pointFour.x
                        }
                    y = pointFour.y
                }
                Knot.Side.RIGHT -> {
                    x = pointFour.x
                    y =
                        if (endPoint.y in startBlockRect.top..startBlockRect.bottom && endPoint.x < startBlockRect.left) {
                            endPoint.y
                        } else {
                            pointFour.y
                        }
                }
                Knot.Side.LEFT -> {
                    x = pointFour.x
                    y =
                        if (endPoint.y in startBlockRect.top..startBlockRect.bottom && endPoint.x > startBlockRect.right) {
                            endPoint.y
                        } else {
                            pointFour.y
                        }
                }
            }
        }

        pointSix.apply {
            x = endPoint.x
            y = endPoint.y
        }

        pointSeven.apply {
            x = endPoint.x
            y = endPoint.y
        }

        pointEnd.apply {
            x = endPoint.x
            y = endPoint.y
        }

        if (endKnot != null) {
            updatePathWithEndKnot(startPoint, endPoint, startKnot, endKnot)
        }

        //Приравнивание положений точек к сетке. Делим на 2, так как необходима возможность
        //взаимодействия с точками центров, которые размещаются в сетке с размером ячеек в два раза меньше
        //стандартной
        pointTwo.apply {
            x = x.roundToValue(gridSize/2)
            y = y.roundToValue(gridSize/2)
        }

        pointThree.apply {
            x = x.roundToValue(gridSize/2)
            y = y.roundToValue(gridSize/2)
        }

        pointFour.apply {
            x = x.roundToValue(gridSize/2)
            y = y.roundToValue(gridSize/2)
        }

        pointFive.apply {
            x = x.roundToValue(gridSize/2)
            y = y.roundToValue(gridSize/2)
        }

        pointSix.apply {
            x = x.roundToValue(gridSize/2)
            y = y.roundToValue(gridSize/2)
        }

        pointSeven.apply {
            x = x.roundToValue(gridSize/2)
            y = y.roundToValue(gridSize/2)
        }

        path.apply {
            reset()
            moveTo(startPoint.x, startPoint.y)
            lineTo(pointTwo.x, pointTwo.y)
            lineTo(pointThree.x, pointThree.y)
            lineTo(pointFour.x, pointFour.y)
            lineTo(pointFive.x, pointFive.y)
            lineTo(pointSix.x, pointSix.y)
            lineTo(pointSeven.x, pointSeven.y)

            lineTo(pointEnd.x, pointEnd.y)
        }
        updateArrow(startPoint, endPoint, startKnot, endKnot)
    }

    private fun updatePathWithEndKnot(startPoint: PointF, endPoint: PointF, startKnot: Knot, endKnot: Knot) {
        val block = startKnot.logicBlockView
        var startBlockRect = RectF().apply {
            left = block.getCenter().x - block.getWidth() / 2
            right = block.getCenter().x + block.getWidth() / 2
            top = block.getCenter().y - block.getHeight() / 2
            bottom = block.getCenter().y + block.getHeight() / 2
        }
        val endBlock = endKnot.logicBlockView
        var endBlockRect = RectF().apply {
            left = endBlock.getCenter().x - endBlock.getWidth() / 2
            right = endBlock.getCenter().x + endBlock.getWidth() / 2
            top = endBlock.getCenter().y - endBlock.getHeight() / 2
            bottom = endBlock.getCenter().y + endBlock.getHeight() / 2
        }
        when (startKnot.side) {
            Knot.Side.BOTTOM -> {
                when (endKnot.side) {
                    Knot.Side.TOP -> {
                        if (endBlockRect.top > startBlockRect.bottom) return
                        if (endPoint.x > startBlockRect.right + blockOffset) {
                            pointThree.x =
                                (startBlockRect.right + (endBlockRect.left - startBlockRect.right) / 2).coerceAtLeast(
                                    startBlockRect.right + blockOffset
                                )

                            pointFour.apply {
                                x = pointThree.x
                                y = pointFour.y.coerceAtMost(endBlockRect.top - blockOffset)
                            }
                            pointFive.apply {
                                x = endPoint.x
                                y = pointFour.y
                            }
                            return
                        }
                        if (endPoint.x < startBlockRect.left - blockOffset) {
                            pointThree.x =
                                (startBlockRect.left - (startBlockRect.left - endBlockRect.right) / 2).coerceAtMost(
                                    startBlockRect.left - blockOffset
                                )

                            pointFour.apply {
                                x = pointThree.x
                                y = pointFour.y.coerceAtMost(endBlockRect.top - blockOffset)
                            }
                            pointFive.apply {
                                x = endPoint.x
                                y = pointFour.y
                            }
                            return
                        } else {
                            pointFour.y =
                                (startBlockRect.top - (startBlockRect.top - endBlockRect.bottom) / 2).coerceAtMost(
                                    startBlockRect.top - blockOffset
                                )
                            pointFive.apply {
                                x = pointFive.x.coerceAtMost(endBlockRect.left - blockOffset)
                                y = pointFour.y
                            }
                            pointSix.apply {
                                x = pointFive.x
                                y = endBlockRect.top - blockOffset
                            }
                            pointSeven.apply {
                                x = endPoint.x
                                y = pointSix.y
                            }
                            return
                        }
                    }
                    Knot.Side.BOTTOM -> {
                        if (endBlockRect.bottom < startBlockRect.top) return
                        pointTwo.apply {
                            y = (startBlockRect.bottom + (endBlockRect.top - startBlockRect.bottom) / 2).coerceAtLeast(
                                startBlockRect.bottom + blockOffset
                            )
                        }
                        pointThree.apply {
                            x = pointTwo.x.coerceOut(endBlockRect.left - blockOffset, endBlockRect.right + blockOffset)
                            y = pointTwo.y
                        }
                        pointFour.apply {
                            x = pointThree.x
                            y = (endBlockRect.bottom + blockOffset).coerceAtLeast(startBlockRect.bottom + blockOffset)
                        }
                        pointFive.apply {
                            x = endPoint.x
                            y = pointFour.y
                        }
                        return
                    }
                    Knot.Side.RIGHT -> {
                        pointTwo.apply {
                            y = (startBlockRect.bottom + (endBlockRect.top - startBlockRect.bottom) / 2).coerceAtLeast(
                                startBlockRect.bottom + blockOffset
                            )
                        }
                        if ((endBlockRect.top - blockOffset > startBlockRect.bottom + blockOffset) || (endPoint.y > startBlockRect.bottom + blockOffset && endPoint.x < startPoint.x)) {
                            pointThree.apply {
                                x = pointTwo.x.coerceAtLeast(endBlockRect.right + blockOffset)
                                y = pointTwo.y
                            }
                            pointFour.apply {
                                x = pointThree.x
                                y = endPoint.y
                            }
                            pointFive.apply {
                                x = endPoint.x
                                y = endPoint.y
                            }
                            return
                        }
                        if (endPoint.x < startBlockRect.left - blockOffset && endPoint.y < startBlockRect.bottom + blockOffset) {
                            pointThree.x = startBlockRect.left - (startBlockRect.left - endPoint.x) / 2
                            pointFour.apply {
                                x = pointThree.x
                                y = endPoint.y
                            }
                            pointFive.apply {
                                x = endPoint.x
                                y = endPoint.y
                            }
                            return
                        }
                        if (endPoint.y < startBlockRect.top && endPoint.x < startPoint.x) {
                            pointFour.apply {
                                y = (startBlockRect.top - (startBlockRect.top - endBlockRect.bottom) / 2).coerceAtMost(
                                    startBlockRect.top + blockOffset
                                )
                            }
                            pointFive.apply {
                                y = pointFour.y
                                x = endBlockRect.right + blockOffset
                            }
                            pointSix.apply {
                                y = endPoint.y
                                x = pointFive.x
                            }
                            return
                        }
                        if (endBlockRect.top - blockOffset < startBlockRect.bottom + blockOffset && endPoint.y > startBlockRect.centerY()) {
                            pointThree.apply {
                                x =
                                    (startBlockRect.right + (endBlockRect.left - startBlockRect.right) / 2).coerceAtMost(
                                        startBlockRect.right + blockOffset
                                    )
                                y = pointTwo.y
                            }
                            pointFour.apply {
                                x = pointThree.x
                                y = endBlockRect.top - blockOffset
                            }
                            pointFive.apply {
                                y = pointFour.y
                                x = endBlockRect.right + blockOffset
                            }
                            pointSix.apply {
                                y = endPoint.y
                                x = pointFive.x
                            }
                            return
                        }
                        pointThree.x = max(endBlockRect.right, startBlockRect.right) + blockOffset
                        pointFour.apply {
                            x = pointThree.x
                            y = endPoint.y
                        }
                        pointFive.apply {
                            x = endPoint.x
                            y = endPoint.y
                        }
                        pointSix.apply {
                            x = endPoint.x
                            y = endPoint.y
                        }
                        return
                    }
                    Knot.Side.LEFT -> {
                        pointTwo.apply {
                            y = (startBlockRect.bottom + (endBlockRect.top - startBlockRect.bottom) / 2).coerceAtLeast(
                                startBlockRect.bottom + blockOffset
                            )
                        }
                        if ((endBlockRect.top - blockOffset > startBlockRect.bottom + blockOffset) || (endPoint.y > startBlockRect.bottom + blockOffset && endPoint.x > startPoint.x)) {
                            pointThree.apply {
                                x = pointTwo.x.coerceAtMost(endBlockRect.left - blockOffset)
                                y = pointTwo.y
                            }
                            pointFour.apply {
                                x = pointThree.x
                                y = endPoint.y
                            }
                            pointFive.apply {
                                x = endPoint.x
                                y = endPoint.y
                            }
                            return
                        }
                        if (endPoint.x > startBlockRect.right + blockOffset && endPoint.y < startBlockRect.bottom + blockOffset) {
                            pointThree.x = startBlockRect.right + (endPoint.x - startBlockRect.right) / 2
                            pointFour.apply {
                                x = pointThree.x
                                y = endPoint.y
                            }
                            pointFive.apply {
                                x = endPoint.x
                                y = endPoint.y
                            }
                            return
                        }
                        if (endPoint.y < startBlockRect.top && endPoint.x > startPoint.x) {
                            pointFour.apply {
                                y = (startBlockRect.top - (startBlockRect.top - endBlockRect.bottom) / 2).coerceAtMost(
                                    startBlockRect.top + blockOffset
                                )
                            }
                            pointFive.apply {
                                y = pointFour.y
                                x = endBlockRect.left - blockOffset
                            }
                            pointSix.apply {
                                y = endPoint.y
                                x = pointFive.x
                            }
                            return
                        }
                        if (endBlockRect.top - blockOffset < startBlockRect.bottom + blockOffset && endPoint.y > startBlockRect.centerY()) {
                            pointThree.apply {
                                x = (startBlockRect.left - (startBlockRect.left - endBlockRect.right) / 2).coerceAtMost(
                                    startBlockRect.left - blockOffset
                                )
                                y = pointTwo.y
                            }
                            pointFour.apply {
                                x = pointThree.x
                                y = endBlockRect.top - blockOffset
                            }
                            pointFive.apply {
                                y = pointFour.y
                                x = endBlockRect.left - blockOffset
                            }
                            pointSix.apply {
                                y = endPoint.y
                                x = pointFive.x
                            }
                            return
                        }
                        pointThree.x = min(endBlockRect.left, startBlockRect.left) - blockOffset
                        pointFour.apply {
                            x = pointThree.x
                            y = endPoint.y
                        }
                        pointFive.apply {
                            x = endPoint.x
                            y = endPoint.y
                        }
                        pointSix.apply {
                            x = endPoint.x
                            y = endPoint.y
                        }
                        return
                    }
                }
            }
            Knot.Side.RIGHT -> {
                pointTwo.x =
                    (startBlockRect.right + (endBlockRect.left - startBlockRect.right) / 2).coerceAtLeast(startBlockRect.right + blockOffset)
                pointThree.y = pointThree.y.coerceAtLeast(startBlockRect.bottom + blockOffset)
                pointThree.x = pointThree.x.coerceAtMost(startBlockRect.right + blockOffset)
                pointFour.y = pointThree.y
                pointFive.y = endPoint.y
                pointSix.y = endPoint.y
                when (endKnot.side) {
                    Knot.Side.TOP -> {
                        if ((endPoint.x > startBlockRect.centerX() && endBlockRect.top > startPoint.y + blockOffset) || endBlockRect.top - blockOffset > startBlockRect.bottom + blockOffset) {
                            if (endPoint.x > startBlockRect.right + blockOffset) {
                                pointThree.y = pointTwo.y
                            } else {
                                pointThree.y =
                                    (startBlockRect.bottom + (endBlockRect.top - startBlockRect.bottom) / 2).coerceAtLeast(
                                        startBlockRect.bottom + blockOffset
                                    )
                            }
                            pointFour.y = pointThree.y
                            return
                        }
                        if (endPoint.x < startBlockRect.left && endBlockRect.top - blockOffset < startBlockRect.bottom + blockOffset && endPoint.y > startBlockRect.top) {
                            pointThree.y = pointThree.y.coerceAtLeast(startBlockRect.bottom + blockOffset)
                            pointFour.x =
                                (startBlockRect.left - (startBlockRect.left - endBlockRect.right) / 2).coerceAtMost(
                                    startBlockRect.left - blockOffset
                                )
                            pointFour.y = pointThree.y
                            pointFive.apply {
                                x = pointFour.x
                                y = endBlockRect.top - blockOffset
                            }
                            pointSix.apply {
                                x = endPoint.x
                                y = pointFive.y
                            }
                            return
                        }
                        if ((endBlockRect.top <= startBlockRect.top && endBlockRect.left < startBlockRect.right + blockOffset)) {
                            pointTwo.x =
                                max(startBlockRect.right + blockOffset, endBlockRect.right + blockOffset).coerceAtLeast(
                                    startBlockRect.right + blockOffset
                                )
                            pointThree.apply {
                                x = pointTwo.x
                                y = endBlockRect.top - blockOffset
                            }
                            pointFour.apply {
                                x = endPoint.x
                                y = pointThree.y
                            }
                            pointFive.apply {
                                x = endPoint.x
                                y = endPoint.y
                            }
                            return
                        }
                        if (endPoint.x >= startBlockRect.right + blockOffset) {
                            pointThree.apply {
                                x = pointTwo.x
                                y = endBlockRect.top - blockOffset
                            }
                            pointFour.apply {
                                x = endPoint.x
                                y = pointThree.y
                            }
                            pointFive.apply {
                                x = endPoint.x
                                y = endPoint.y
                            }
                        }
                    }
                    Knot.Side.BOTTOM -> {
                        if ((endPoint.x > startBlockRect.centerX() && endBlockRect.bottom < startPoint.y - blockOffset) || endBlockRect.bottom + blockOffset < startBlockRect.top - blockOffset) {
                            if (endPoint.x > startBlockRect.right + blockOffset) {
                                pointThree.y = pointTwo.y
                            } else {
                                pointThree.y =
                                    (startBlockRect.top - (startBlockRect.top - endBlockRect.bottom) / 2).coerceAtMost(
                                        startBlockRect.top - blockOffset
                                    )
                            }
                            pointFour.y = pointThree.y
                            return
                        }
                        if (endPoint.x < startBlockRect.left && endBlockRect.bottom + blockOffset > startBlockRect.top - blockOffset && endPoint.y < startBlockRect.bottom) {
                            pointThree.y = pointThree.y.coerceAtMost(startBlockRect.top - blockOffset)
                            pointFour.x =
                                (startBlockRect.left - (startBlockRect.left - endBlockRect.right) / 2).coerceAtMost(
                                    startBlockRect.left - blockOffset
                                )
                            pointFour.y = pointThree.y
                            pointFive.apply {
                                x = pointFour.x
                                y = endBlockRect.bottom + blockOffset
                            }
                            pointSix.apply {
                                x = endPoint.x
                                y = pointFive.y
                            }
                            return
                        }
                        if ((endBlockRect.bottom >= startBlockRect.bottom && endBlockRect.left < startBlockRect.right + blockOffset)) {
                            pointTwo.x =
                                max(startBlockRect.right + blockOffset, endBlockRect.right + blockOffset).coerceAtLeast(
                                    startBlockRect.right + blockOffset
                                )
                            pointThree.apply {
                                x = pointTwo.x
                                y = endBlockRect.bottom + blockOffset
                            }
                            pointFour.apply {
                                x = endPoint.x
                                y = pointThree.y
                            }
                            pointFive.apply {
                                x = endPoint.x
                                y = endPoint.y
                            }
                            return
                        }
                        if (endPoint.x >= startBlockRect.right + blockOffset) {
                            pointThree.apply {
                                x = pointTwo.x
                                y = endBlockRect.bottom + blockOffset
                            }
                            pointFour.apply {
                                x = endPoint.x
                                y = pointThree.y
                            }
                            pointFive.apply {
                                x = endPoint.x
                                y = endPoint.y
                            }
                        }
                    }
                    Knot.Side.LEFT -> {
                        if (endBlockRect.left - blockOffset > startBlockRect.right + blockOffset) {
                            pointThree.apply {
                                x = pointTwo.x
                                y = endPoint.y
                            }
                            pointFour.apply {
                                x = pointThree.x
                                y = pointThree.y
                            }
                            return
                        }
                        if (endPoint.y < startPoint.y) {
                            pointThree.apply {
                                x = pointTwo.x
                                y = (startBlockRect.top - (startBlockRect.top - endBlockRect.bottom)/2).coerceAtMost(startBlockRect.top - blockOffset)
                            }
                        } else {
                            pointThree.apply {
                                x = pointTwo.x
                                y = (startBlockRect.bottom + (endBlockRect.top - startBlockRect.bottom)/2).coerceAtLeast(startBlockRect.bottom + blockOffset)
                            }
                        }

                        if (endBlockRect.left < startBlockRect.left) {
                            pointFour.apply {
                                x = (startBlockRect.left - (startBlockRect.left - endBlockRect.right)/2).coerceAtMost(startBlockRect.left - blockOffset)
                                y = pointThree.y
                            }

                            if (endPoint.y < startPoint.y) {
                                pointFive.apply {
                                    x = pointFour.x
                                    y = pointFour.y.coerceAtLeast(endBlockRect.bottom + blockOffset)
                                }
                            } else {
                                pointFive.apply {
                                    x = pointFour.x
                                    y = pointFour.y.coerceAtMost(endBlockRect.top - blockOffset)
                                }
                            }
                            pointSix.apply {
                                x = endBlockRect.left - blockOffset
                                y = pointFive.y
                            }
                        } else {
                            pointFour.apply {
                                x = endBlockRect.left - blockOffset
                                y = pointThree.y
                            }

                            pointFive.apply {
                                x = pointFour.x
                                y = pointThree.y
                            }

                            pointSix.apply {
                                x = pointFour.x
                                y = pointThree.y
                            }
                        }

                        pointSeven.apply {
                            x = pointSix.x
                            y = endPoint.y
                        }

                    }
                    Knot.Side.RIGHT -> {
                        if (endBlockRect.right >= startBlockRect.left || endBlockRect.bottom <= startBlockRect.top || endBlockRect.top >= startBlockRect.bottom) {
                            if (endBlockRect.right > startBlockRect.right) {
                                pointThree.apply {
                                    x = pointTwo.x
                                    y = pointTwo.y.coerceOut(endBlockRect.top - blockOffset, endBlockRect.bottom + blockOffset)
                                }
                            } else {
                                pointThree.apply {
                                    x = pointTwo.x
                                    y = pointTwo.y
                                }
                            }
                            pointFour.apply {
                                x = (endBlockRect.right + blockOffset).coerceAtLeast(pointTwo.x)
                                y = pointThree.y
                            }
                            pointFive.apply {
                                x = pointFour.x
                                y = endPoint.y
                            }
                            return
                        }
                        pointThree.apply {
                            x = pointTwo.x
                            y = if (endPoint.y < startPoint.y) {
                                endPoint.y.coerceAtMost(startBlockRect.top - blockOffset)
                            } else {
                                endPoint.y.coerceAtLeast(startBlockRect.bottom + blockOffset)
                            }
                        }
                        pointFour.apply {
                            x = (startBlockRect.left - (startBlockRect.left - endBlockRect.right)/2).coerceAtMost(startBlockRect.left - blockOffset)
                            y = pointThree.y
                        }

                        pointFive.apply {
                            x = pointFour.x
                            y = endPoint.y
                        }
                    }
                }
            }
            Knot.Side.LEFT -> {
                pointTwo.x =
                    (startBlockRect.left - (startBlockRect.left - endBlockRect.right) / 2).coerceAtMost(startBlockRect.left - blockOffset)
                pointThree.y = pointThree.y.coerceAtLeast(startBlockRect.bottom + blockOffset)
                pointThree.x = pointThree.x.coerceAtMost(startBlockRect.left - blockOffset)
                pointFour.y = pointThree.y
                pointFive.y = endPoint.y
                pointSix.y = endPoint.y
                when (endKnot.side) {
                    Knot.Side.LEFT -> {
                        if (endBlockRect.left <= startBlockRect.right || endBlockRect.bottom <= startBlockRect.top || endBlockRect.top >= startBlockRect.bottom) {
                            if (endBlockRect.left < startBlockRect.left) {
                                pointThree.apply {
                                    x = pointTwo.x
                                    y = pointTwo.y.coerceOut(endBlockRect.top - blockOffset, endBlockRect.bottom + blockOffset)
                                }
                            } else {
                                pointThree.apply {
                                    x = pointTwo.x
                                    y = pointTwo.y
                                }
                            }
                            pointFour.apply {
                                x = (endBlockRect.left - blockOffset).coerceAtMost(pointTwo.x)
                                y = pointThree.y
                            }
                            pointFive.apply {
                                x = pointFour.x
                                y = endPoint.y
                            }
                            return
                        }
                        pointThree.apply {
                            x = pointTwo.x
                            y = if (endPoint.y < startPoint.y) {
                                endPoint.y.coerceAtMost(startBlockRect.top - blockOffset)
                            } else {
                                endPoint.y.coerceAtLeast(startBlockRect.bottom + blockOffset)
                            }
                        }
                        pointFour.apply {
                            x = (startBlockRect.right + (endBlockRect.left - startBlockRect.right)/2).coerceAtMost(startBlockRect.right + blockOffset)
                            y = pointThree.y
                        }

                        pointFive.apply {
                            x = pointFour.x
                            y = endPoint.y
                        }
                    }
                    Knot.Side.RIGHT -> {
                        if (endBlockRect.right + blockOffset < startBlockRect.left - blockOffset) {
                            pointThree.apply {
                                x = pointTwo.x
                                y = endPoint.y
                            }
                            pointFour.apply {
                                x = pointThree.x
                                y = pointThree.y
                            }
                            return
                        }
                        if (endPoint.y < startPoint.y) {
                            pointThree.apply {
                                x = pointTwo.x
                                y = (startBlockRect.top - (startBlockRect.top - endBlockRect.bottom)/2).coerceAtMost(startBlockRect.top - blockOffset)
                            }
                        } else {
                            pointThree.apply {
                                x = pointTwo.x
                                y = (startBlockRect.bottom + (endBlockRect.top - startBlockRect.bottom)/2).coerceAtLeast(startBlockRect.bottom + blockOffset)
                            }
                        }

                        if (endBlockRect.right > startBlockRect.right) {
                            pointFour.apply {
                                x = (startBlockRect.right + (endBlockRect.left - startBlockRect.right)/2).coerceAtLeast(startBlockRect.right + blockOffset)
                                y = pointThree.y
                            }

                            if (endPoint.y < startPoint.y) {
                                pointFive.apply {
                                    x = pointFour.x
                                    y = pointFour.y.coerceAtLeast(endBlockRect.bottom + blockOffset)
                                }
                            } else {
                                pointFive.apply {
                                    x = pointFour.x
                                    y = pointFour.y.coerceAtMost(endBlockRect.top - blockOffset)
                                }
                            }
                            pointSix.apply {
                                x = endBlockRect.right + blockOffset
                                y = pointFive.y
                            }
                        } else {
                            pointFour.apply {
                                x = endBlockRect.right + blockOffset
                                y = pointThree.y
                            }

                            pointFive.apply {
                                x = pointFour.x
                                y = pointThree.y
                            }

                            pointSix.apply {
                                x = pointFour.x
                                y = pointThree.y
                            }
                        }

                        pointSeven.apply {
                            x = pointSix.x
                            y = endPoint.y
                        }
                    }
                    Knot.Side.TOP -> {
                        if ((endPoint.x < startBlockRect.centerX() && endBlockRect.top > startPoint.y + blockOffset) || endBlockRect.top - blockOffset > startBlockRect.bottom + blockOffset) {
                            if (endPoint.x < startBlockRect.left - blockOffset) {
                                pointThree.y = pointTwo.y
                            } else {
                                pointThree.y =
                                    (startBlockRect.bottom + (endBlockRect.top - startBlockRect.bottom) / 2).coerceAtLeast(
                                        startBlockRect.bottom + blockOffset
                                    )
                            }
                            pointFour.y = pointThree.y
                            return
                        }
                        if (endPoint.x > startBlockRect.right && endBlockRect.top - blockOffset < startBlockRect.bottom + blockOffset && endPoint.y > startBlockRect.top) {
                            pointThree.y = pointThree.y.coerceAtLeast(startBlockRect.bottom + blockOffset)
                            pointFour.x =
                                (startBlockRect.right + (endBlockRect.left - startBlockRect.right) / 2).coerceAtLeast(
                                    startBlockRect.right + blockOffset
                                )
                            pointFour.y = pointThree.y
                            pointFive.apply {
                                x = pointFour.x
                                y = endBlockRect.top - blockOffset
                            }
                            pointSix.apply {
                                x = endPoint.x
                                y = pointFive.y
                            }
                            return
                        }
                        if ((endBlockRect.top <= startBlockRect.top && endBlockRect.right > startBlockRect.left - blockOffset)) {
                            pointTwo.x =
                                min(startBlockRect.left - blockOffset, endBlockRect.left - blockOffset).coerceAtMost(
                                    startBlockRect.left - blockOffset
                                )
                            pointThree.apply {
                                x = pointTwo.x
                                y = endBlockRect.top - blockOffset
                            }
                            pointFour.apply {
                                x = endPoint.x
                                y = pointThree.y
                            }
                            pointFive.apply {
                                x = endPoint.x
                                y = endPoint.y
                            }
                            return
                        }
                        if (endPoint.x <= startBlockRect.left - blockOffset) {
                            pointThree.apply {
                                x = pointTwo.x
                                y = endBlockRect.top - blockOffset
                            }
                            pointFour.apply {
                                x = endPoint.x
                                y = pointThree.y
                            }
                            pointFive.apply {
                                x = endPoint.x
                                y = endPoint.y
                            }
                        }
                    }
                    Knot.Side.BOTTOM -> {
                        if ((endPoint.x < startBlockRect.centerX() && endBlockRect.bottom < startPoint.y - blockOffset) || endBlockRect.bottom + blockOffset < startBlockRect.top - blockOffset) {
                            if (endPoint.x < startBlockRect.left - blockOffset) {
                                pointThree.y = pointTwo.y
                            } else {
                                pointThree.y =
                                    (startBlockRect.top - (startBlockRect.top - endBlockRect.bottom) / 2).coerceAtMost(
                                        startBlockRect.top - blockOffset
                                    )
                            }
                            pointFour.y = pointThree.y
                            return
                        }
                        if (endPoint.x > startBlockRect.right && endBlockRect.bottom + blockOffset > startBlockRect.top - blockOffset && endPoint.y < startBlockRect.bottom) {
                            pointThree.y = pointThree.y.coerceAtMost(startBlockRect.top - blockOffset)
                            pointFour.x =
                                (startBlockRect.right + (endBlockRect.left - startBlockRect.right) / 2).coerceAtLeast(
                                    startBlockRect.right + blockOffset
                                )
                            pointFour.y = pointThree.y
                            pointFive.apply {
                                x = pointFour.x
                                y = endBlockRect.bottom + blockOffset
                            }
                            pointSix.apply {
                                x = endPoint.x
                                y = pointFive.y
                            }
                            return
                        }
                        if ((endBlockRect.bottom >= startBlockRect.bottom && endBlockRect.right > startBlockRect.left - blockOffset)) {
                            pointTwo.x =
                                min(startBlockRect.left - blockOffset, endBlockRect.left - blockOffset).coerceAtMost(
                                    startBlockRect.left - blockOffset
                                )
                            pointThree.apply {
                                x = pointTwo.x
                                y = endBlockRect.bottom + blockOffset
                            }
                            pointFour.apply {
                                x = endPoint.x
                                y = pointThree.y
                            }
                            pointFive.apply {
                                x = endPoint.x
                                y = endPoint.y
                            }
                            return
                        }
                        if (endPoint.x <= startBlockRect.left - blockOffset) {
                            pointThree.apply {
                                x = pointTwo.x
                                y = endBlockRect.bottom + blockOffset
                            }
                            pointFour.apply {
                                x = endPoint.x
                                y = pointThree.y
                            }
                            pointFive.apply {
                                x = endPoint.x
                                y = endPoint.y
                            }
                        }
                    }
                }
            }
            else -> {}
        }
    }
    private fun updateArrow(startPoint: PointF, endPoint: PointF, startKnot: Knot, endKnot: Knot?) {
        val block = startKnot.logicBlockView
        var startBlockRect = RectF().apply {
            left = block.getCenter().x - block.getWidth() / 2
            right = block.getCenter().x + block.getWidth() / 2
            top = block.getCenter().y - block.getHeight() / 2
            bottom = block.getCenter().y + block.getHeight() / 2
        }

        fun addRightArrow() {
            path.apply {
                moveTo(endPoint.x - arrowWidth, endPoint.y - arrowHeight/2)
                lineTo(endPoint.x, endPoint.y)
                lineTo(endPoint.x - arrowWidth, endPoint.y + arrowHeight/2)
            }
        }

        fun addLeftArrow() {
            path.apply {
                moveTo(endPoint.x + arrowWidth, endPoint.y - arrowHeight/2)
                lineTo(endPoint.x, endPoint.y)
                lineTo(endPoint.x + arrowWidth, endPoint.y + arrowHeight/2)
            }
        }

        fun addTopArrow() {
            path.apply {
                moveTo(endPoint.x - arrowHeight/2, endPoint.y + arrowWidth)
                lineTo(endPoint.x, endPoint.y)
                lineTo(endPoint.x + arrowHeight/2, endPoint.y + arrowWidth)
            }
        }

        fun addBottomArrow() {
            path.apply {
                moveTo(endPoint.x - arrowHeight/2, endPoint.y - arrowWidth)
                lineTo(endPoint.x, endPoint.y)
                lineTo(endPoint.x + arrowHeight/2, endPoint.y - arrowWidth)
            }
        }

        if (endKnot != null) {
            when(endKnot.side) {
                Knot.Side.RIGHT -> addLeftArrow()
                Knot.Side.LEFT -> addRightArrow()
                Knot.Side.TOP -> addBottomArrow()
                Knot.Side.BOTTOM -> addTopArrow()
            }
        } else {
            if (endPoint.y < startBlockRect.top) {
                addTopArrow()
            } else if (endPoint.y > startBlockRect.bottom) {
                addBottomArrow()
            } else if (endPoint.x > startBlockRect.right) {
                addRightArrow()
            } else {
                addLeftArrow()
            }
        }
    }

}