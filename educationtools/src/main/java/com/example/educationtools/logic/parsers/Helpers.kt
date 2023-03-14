package com.example.educationtools.logic.parsers

import com.example.educationtools.logic.MemoryModel
import com.example.educationtools.logic.functions.Function
import com.example.educationtools.logic.functions.TypeFunction
import com.example.educationtools.logic.functions.VariableFunction
import com.example.educationtools.logic.methods.ConditionsF
import com.example.educationtools.logic.methods.MathF
import com.example.educationtools.utils.UNDECLARED_VARIABLE_TEXT
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

val SPACES = """\s+""".toRegex()
val VARIABLE_TEMPLATE = """^[a-zA-Z]\w*""".toRegex()
val VARIABLE_SYMBOL_TEMPLATE = """\w""".toRegex()
val LETTER_TEMPLATE = """[a-zA-Z]""".toRegex()
val INT_TEMPLATE = """\d+""".toRegex()
val FLOAT_TEMPLATE = """\d+\.{1}\d+""".toRegex()
val METHOD_TEMPLATE = """^[a-zA-Z]\w*\(.*\)""".toRegex()
const val VARIABLE_ATTRIBUTES_COUNT = 2
const val CALCULATION_OPERANDS_COUNT = 2



val types = mapOf<String, KClass<*>>("int" to Int::class, "float" to Float::class)

val calculationMethodsMap = mapOf<String, Map<List<KClass<*>>, KFunction<*>>>(
    "sum" to mapOf(
        listOf(Int::class, Int::class) to MathF.Companion::sumII,
        listOf(Int::class, Float::class) to MathF.Companion::sumIF,
        listOf(Float::class, Int::class) to MathF.Companion::sumFI,
        listOf(Float::class, Float::class) to MathF.Companion::sumFF,
    ),
    "min" to mapOf(
        listOf(Int::class, Int::class) to MathF.Companion::minusII,
        listOf(Int::class, Float::class) to MathF.Companion::minusIF,
        listOf(Float::class, Int::class) to MathF.Companion::minusFI,
        listOf(Float::class, Float::class) to MathF.Companion::minusFF,
    ),
    "mult" to mapOf(
        listOf(Int::class, Int::class) to MathF.Companion::timeII,
        listOf(Int::class, Float::class) to MathF.Companion::timeIF,
        listOf(Float::class, Int::class) to MathF.Companion::timeFI,
        listOf(Float::class, Float::class) to MathF.Companion::timeFF,
    ),
    "div" to mapOf(
        listOf(Int::class, Int::class) to MathF.Companion::divII,
        listOf(Int::class, Float::class) to MathF.Companion::divIF,
        listOf(Float::class, Int::class) to MathF.Companion::divFI,
        listOf(Float::class, Float::class) to MathF.Companion::divFF,
    ),

    "mod" to mapOf(
        listOf(Int::class, Int::class) to MathF.Companion::modII
    )
)

val conditionMethodsMap = mapOf (
    ">" to mapOf(
        listOf(Int::class, Int::class) to ConditionsF.Companion::moreII,
        listOf(Int::class, Float::class) to ConditionsF.Companion::moreIF,
        listOf(Float::class, Int::class) to ConditionsF.Companion::moreFI,
        listOf(Float::class, Float::class) to ConditionsF.Companion::moreFF,
    ),

    "<" to mapOf(
        listOf(Int::class, Int::class) to ConditionsF.Companion::lessII,
        listOf(Int::class, Float::class) to ConditionsF.Companion::lessIF,
        listOf(Float::class, Int::class) to ConditionsF.Companion::lessFI,
        listOf(Float::class, Float::class) to ConditionsF.Companion::lessFF,
    ),

    ">=" to mapOf(
        listOf(Int::class, Int::class) to ConditionsF.Companion::moreOrEqualII,
        listOf(Int::class, Float::class) to ConditionsF.Companion::moreOrEqualIF,
        listOf(Float::class, Int::class) to ConditionsF.Companion::moreOrEqualFI,
        listOf(Float::class, Float::class) to ConditionsF.Companion::moreOrEqualFF,
    ),

    "<=" to mapOf(
        listOf(Int::class, Int::class) to ConditionsF.Companion::lessOrEqualII,
        listOf(Int::class, Float::class) to ConditionsF.Companion::lessOrEqualIF,
        listOf(Float::class, Int::class) to ConditionsF.Companion::lessOrEqualFI,
        listOf(Float::class, Float::class) to ConditionsF.Companion::lessOrEqualFF,
    ),

    "==" to mapOf(
        listOf(Int::class, Int::class) to ConditionsF.Companion::equalII,
        listOf(Int::class, Float::class) to ConditionsF.Companion::equalIF,
        listOf(Float::class, Int::class) to ConditionsF.Companion::equalFI,
        listOf(Float::class, Float::class) to ConditionsF.Companion::equalFF,
    ),

    "!=" to mapOf(
        listOf(Int::class, Int::class) to ConditionsF.Companion::notEqualII,
        listOf(Int::class, Float::class) to ConditionsF.Companion::notEqualIF,
        listOf(Float::class, Int::class) to ConditionsF.Companion::notEqualFI,
        listOf(Float::class, Float::class) to ConditionsF.Companion::notEqualFF,
    ),

    "&&" to mapOf(
        listOf(Boolean::class, Boolean::class) to ConditionsF.Companion::andF,
    ),

    "||" to mapOf(
        listOf(Boolean::class, Boolean::class) to ConditionsF.Companion::orF,
    )
)

fun getComaSeparatorIndexes(text: String): List<Int> {
    var i = 0
    var bracket = 0
    val result = mutableListOf<Int>()
    while (i < text.length) {
        if (text[i] == '(') {
            bracket++
        }
        if (text[i] == ')') {
            bracket--
        }
        if (bracket == 0 && text[i] == ',') {
            result.add(i)
        }
        i++
    }
    return result
}

fun parseVariableFunction(text: String, availableVariables: List<String>, memoryModel: MemoryModel): Function? {
    if (VARIABLE_TEMPLATE.matches(text)) {
        if (text in availableVariables) {
            return VariableFunction(memoryModel, text)
        } else {
            throw Exception(UNDECLARED_VARIABLE_TEXT)
        }
    }
    return null
}

fun parseTypeFunction(text: String): Function? {
    if (INT_TEMPLATE.matches(text)) return TypeFunction.generateTypeFunction(text.toInt())
    if (FLOAT_TEMPLATE.matches(text)) return TypeFunction.generateTypeFunction(text.toFloat())
    return null
}