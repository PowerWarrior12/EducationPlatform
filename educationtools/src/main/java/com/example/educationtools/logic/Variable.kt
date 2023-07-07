package com.example.educationtools.logic

import kotlin.reflect.KClass

data class Variable(
    var name: String = "",
    val type: KClass<*>,
    var value: Any? = null
)
