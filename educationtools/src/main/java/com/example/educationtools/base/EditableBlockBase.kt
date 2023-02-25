package com.example.educationtools.base

import android.graphics.*
import android.text.Layout
import android.text.TextPaint
import android.util.Log
import com.example.educationtools.selection.Selectable
import com.example.educationtools.selection.Selector
import com.example.educationtools.selection.SimpleSelector
import com.example.educationtools.utils.drawBlockText

const val defaultCenterX = 0f
const val defaultCenterY = 0f
const val textMargin = 10f
const val NOT_PARENT_MESSAGE = "Missing a editor parent"
val TAG = EditableBlockBase::class.simpleName

/**
 * @property V - Текущий класс, построить который необходимо создать
 * @property T - Конфигурации, на основе которых необходимо создать блок
 */
abstract class EditableBlockBase protected constructor() : EditableBlock, Selectable {
    //Sizes
    private val center: PointF = PointF(defaultCenterX, defaultCenterY)
    private var width = 0f
    private var height = 0f

    //Text
    private var text: String =
        "Какой то текст"

    private var selector: Selector = SimpleSelector(this)

    private var parentEditor: ParentEditor? = null

    //Rects
    protected val mainRect: RectF
    private val textRect: RectF

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
            selector.drawSelection(this)
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

    override fun getSelector(): Selector {
        return selector
    }

    override fun setEditorParent(parentEditor: ParentEditor) {
        this.parentEditor = parentEditor
    }

    /**
     * Перерисовывает экран, необходимо обязательно инициализировать parentEditor
     */
    override fun invalidate() {
        parentEditor?.invalidate() ?: Log.w(TAG, NOT_PARENT_MESSAGE)
    }

    override fun setText(newText: String) {
        text = newText
    }

    override fun getCenter(): PointF {
        return center
    }

    override fun getHeight(): Float {
        return height
    }

    override fun getText(): String {
        return text
    }

    override fun getWidth(): Float {
        return width
    }

    private fun updateMainRect() {
        mainRect.apply {
            left = center.x - width / 2
            right = center.x + width / 2
            top = center.y - height / 2
            bottom = center.y + height / 2
        }
    }

    open fun updateTextRect() {
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
            drawBlockText(
                text,
                textPaint,
                textRect.width().toInt(),
                textRect,
                horizontalAlignment = Layout.Alignment.ALIGN_CENTER,
                verticalAlignment = Layout.Alignment.ALIGN_CENTER
            )
        }
    }
}

interface EditableBlockFactory<V : EditableBlock> {
    fun create(): EditableBlock
}