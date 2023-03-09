package com.example.educationtools.logic.parsers

import kotlin.reflect.KClass

val SPACES = """\s+""".toRegex()
val VARIABLE_TEMPLATE = """^[a-zA-Z]\w*""".toRegex()
const val VARIABLE_ATTRIBUTES_COUNT = 2
const val SYNTAX_ERROR_TEXT = "Syntax error"
val types = mapOf<String, KClass<*>>("int" to Int::class, "float" to Float::class)