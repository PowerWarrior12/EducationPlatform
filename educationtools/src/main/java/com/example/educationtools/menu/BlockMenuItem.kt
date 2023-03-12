package com.example.educationtools.menu

import com.example.educationtools.base.EditableBlockBase
import com.example.educationtools.base.EditableBlockFactory

class BlockMenuItem(
    val blockFactory: EditableBlockFactory<EditableBlockBase>,
    val icon: Int,
    val title: String
)