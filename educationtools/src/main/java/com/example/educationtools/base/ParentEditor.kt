package com.example.educationtools.base

import com.example.educationtools.logic.MemoryModel

interface ParentEditor {
    fun invalidate()
    fun getMemoryModel(): MemoryModel
    fun deleteBlock(block: EditableBlock)
    fun getGridSize(): Float
    fun getScale(): Float
}