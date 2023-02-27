package com.example.educationtools.logic

import kotlin.reflect.KClass

data class Variable(
    var id: Int = -1,
    var name: String = "",
    val type: KClass<*>,
    var value: Any?
)
