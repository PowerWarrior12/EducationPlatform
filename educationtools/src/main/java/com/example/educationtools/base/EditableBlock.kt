package com.example.educationtools.base

import android.graphics.Canvas
import android.graphics.Point

interface EditableBlock {
    /**
     * Отрисовка фигуры на переданном канвасе
     */
    fun draw(canvas: Canvas)

    /**
     * Проверка принадлежности точки блоку
     * @param point Точка, принадлежность области блоку которой необходимо проверить
     */
    fun checkPointConsists(point: Point): Boolean

}