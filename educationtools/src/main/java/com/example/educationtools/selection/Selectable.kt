package com.example.educationtools.selection

import com.example.educationtools.touching.Touchable

interface Selectable: Touchable {
    fun getSelector(): Selector
}