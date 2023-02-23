package com.example.educationtools.base

import android.graphics.*
import android.text.TextPaint
import android.util.Log
import com.example.educationtools.utils.drawMultilineText

const val defaultCenterX = 0f
const val defaultCenterY = 0f
const val textMargin = 10f
const val NOT_PARENT_MESSAGE = "Missing a editor parent"
val TAG = EditableBlockBase::class.simpleName
/**
 * @property V - Текущий класс, построить который необходимо создать
 * @property T - Конфигурации, на основе которых необходимо создать блок
 */
abstract class EditableBlockBase protected constructor(): EditableBlock{
    //Sizes
    val center: PointF = PointF(defaultCenterX, defaultCenterY)
    var width = 0f
        private set
    var height = 0f
        private set

    //Text
    var text: String = "МОЯ ВОЛЯ МОЯ ВОЛЯ МОЯ ВОЛЯ МОЯ ВОЛЯ МОЯ ВОЛЯ МОЯ ВОЛЯ МОЯ ВОЛЯ МОЯ ВОЛЯ МОЯ ВОЛЯ МОЯ ВОЛЯ"
        private set

    private var selector: Selector? = null

    private var parentEditor: ParentEditor? = null

    //Rects
    protected val mainRect: RectF
    protected val textRect: RectF

    //Paints

    protected val textPaint = TextPaint().apply {
        color = Color.BLACK
        isAntiAlias = true
        textSize = 30f
    }

    init {
        //Main rect
        mainRect = RectF(
            center.x - width / 2,
            center.y - height / 2,
            center.x + width / 2,
            center.y + height / 2
        )

        //Text maximum rect
        textRect = RectF(
            mainRect.left + textMargin,
            mainRect.top + textMargin,
            mainRect.right - textMargin,
            mainRect.bottom - textMargin
        )
    }

    override fun draw(canvas: Canvas) {
        canvas.apply {
            drawFigure(this)
            drawText(this)
        }
    }

    override fun updatePosition(newPosition: PointF) {
        center.apply {
            x = newPosition.x
            y = newPosition.y
        }
        updateMainRect()
        updateTextRect()
        invalidate()
    }

    override fun updateSize(newWidth: Float, newHeight: Float) {
        width = newWidth
        height = newHeight
        updateMainRect()
        updateTextRect()
        invalidate()
    }

    override fun getSelector(): Selector? {
        return selector
    }

    override fun setEditorParent(parentEditor: ParentEditor) {
        this.parentEditor = parentEditor
    }

    /**
     * Перерисовывает экран, необходимо обязательно инициализировать _parentEditor
     */
    override fun invalidate() {
        parentEditor?.invalidate() ?: Log.w(TAG, NOT_PARENT_MESSAGE)
    }

    override fun setText(newText: String) {
        text = newText
    }

    private fun updateMainRect() {
        mainRect.apply {
            left = center.x - width / 2
            right = center.x + width / 2
            top = center.y - height / 2
            bottom = center.y + height / 2
        }
    }

    private fun updateTextRect() {
        textRect.apply {
            left = mainRect.left + textMargin
            top = mainRect.top + textMargin
            right = mainRect.right - textMargin
            bottom = mainRect.bottom - textMargin
        }
    }

    abstract fun drawFigure(canvas: Canvas)
    open fun drawText(canvas: Canvas) {
        canvas.apply {
            drawMultilineText(text, textPaint, textRect.width().toInt(), textRect.left, textRect.centerY())
        }
    }


}
interface EditableBlockFactory<V : EditableBlock> {
    fun create(): EditableBlock
}