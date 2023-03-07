package com.example.educationtools.base

import android.animation.PointFEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.graphics.withScale
import androidx.core.graphics.withTranslation
import com.example.educationtools.blocks.LogicBlockView
import com.example.educationtools.connection.ConnectionManager
import com.example.educationtools.dragging.DragAndDropManager
import com.example.educationtools.logic.LogicBlock
import com.example.educationtools.logic.MemoryModel
import com.example.educationtools.selection.SelectManager
import com.example.educationtools.touching.TouchManager
import com.example.educationtools.touching.Touchable
import com.example.educationtools.utils.Transformations

const val SCROLL_VELOCITY_FACTOR = 0.005f
const val FLING_DURATION = 100L

class EditorViewBase @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), ParentEditor {

    //Данные о изменениях экрана
    private val transformations = Transformations(0f, 0f)
    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private val gestureDetector = GestureDetector(context, ScrollListener())

    //Дочерние блоки
    private val children: MutableList<EditableBlock> = mutableListOf()

    private val memoryModel = MemoryModel()

    //Managers
    private val touchManager = TouchManager(transformations)
    private val selectManager = SelectManager(touchManager)
    private val dragAndDropManager = DragAndDropManager(touchManager, transformations)
    private val connectionManager = ConnectionManager(memoryModel, touchManager, this)

    init {
        selectManager.start()
        dragAndDropManager.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        transformations.updateSize(width.toFloat(), height.toFloat())
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            withTranslation(
                x = transformations.translation.x,
                y = transformations.translation.y
            ) {
                withScale(
                    x = transformations.scale,
                    y = transformations.scale,
                    pivotX = width/2 - transformations.translation.x,
                    pivotY = height/2 - transformations.translation.y
                ) {
                    connectionManager.draw(this)
                    children.forEach {
                        it.draw(this)
                    }
                }
            }
        }
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        return processTouch(event)
    }

    fun addChild(child: EditableBlock) {
        children.add(child)
        child.setEditorParent(this)
        if (child is Touchable) {
            touchManager.addTouchable(child)
        }
        if (child is LogicBlockView) {
            child.setMemoryModel(memoryModel)
            connectionManager.addLogicBlockView(child)
        }
        invalidate()
    }

    /**
     * Обработка тачей
     */
    private fun processTouch(event: MotionEvent): Boolean {
        return if (event.pointerCount > 1) {
            scaleGestureDetector.onTouchEvent(event)
        } else {
            if (event.action == MotionEvent.ACTION_MOVE) {
                touchManager.move(event.x, event.y)
            } else if (event.action == MotionEvent.ACTION_UP) {
                touchManager.releaseTouch(event.x, event.y)
            }
            gestureDetector.onTouchEvent(event)
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            transformations.addScale(detector.scaleFactor)
            invalidate()
            return true
        }
    }

    private inner class ScrollListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            touchManager.touchDown(e.x, e.y)
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            touchManager.longTouch(e.x, e.y)
            super.onLongPress(e)
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            touchManager.singleTouch(e.x, e.y)
            invalidate()
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            animateFling(PointF(-velocityX * SCROLL_VELOCITY_FACTOR, -velocityY * SCROLL_VELOCITY_FACTOR)) { dXY ->
                transformations.addTranslation(dXY.x, dXY.y)
                invalidate()
            }
            return true
        }

        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (!touchManager.isProcess()) {
                transformations.addTranslation(
                    distanceX,
                    distanceY
                )
                invalidate()
                return true
            }
            return false
        }

        private fun animateFling(velocity: PointF, update: (PointF) -> Unit) {
            ValueAnimator.ofObject(PointFEvaluator(), velocity, PointF()).apply {
                interpolator = AccelerateInterpolator()
                duration = FLING_DURATION
                addUpdateListener { animator ->
                    update(animator.animatedValue as PointF)
                }
            }.start()
        }
    }
}