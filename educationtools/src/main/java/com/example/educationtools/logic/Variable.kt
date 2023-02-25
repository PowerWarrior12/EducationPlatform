package com.example.educationtools.logic

data class Variable(
    var id: Int = -1,
    var name: String = "",
    val type: String,
    val value: Any
)
