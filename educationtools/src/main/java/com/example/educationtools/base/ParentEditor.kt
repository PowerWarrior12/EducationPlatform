package com.example.educationtools.base

import com.example.educationtools.logic.MemoryModel

interface ParentEditor {
    fun invalidate()
    fun getMemoryModel(): MemoryModel
}