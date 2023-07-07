package com.example.educationtools.logic.parsers

import com.example.educationtools.logic.MemoryModel
import com.example.educationtools.logic.functions.Function
import com.example.educationtools.logic.functions.ReflectFunction
import com.example.educationtools.logic.functions.TypeFunction
import com.example.educationtools.logic.functions.VariableFunction
import com.example.educationtools.logic.methods.ArrayF
import com.example.educationtools.logic.methods.ConditionsF
import com.example.educationtools.logic.methods.MathF
import com.example.educationtools.utils.SYNTAX_ERROR_TEXT
import com.example.educationtools.utils.UNDECLARED_VARIABLE_TEXT
import com.example.educationtools.utils.extensions.split
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

val SPACES = """\s+""".toRegex()
val VARIABLE_TEMPLATE = """^[a-zA-Z]\w*""".toRegex()
val VARIABLE_SYMBOL_TEMPLATE = """\w""".toRegex()
val LETTER_TEMPLATE = """[a-zA-Z]""".toRegex()
val INT_TEMPLATE = """\d+""".toRegex()
val FLOAT_TEMPLATE = """\d+\.{1}\d+""".toRegex()
val METHOD_TEMPLATE = """^[a-zA-Z]\w*\(.*\)""".toRegex()
val ARRAY_ELEMENT_TEMPLATE = """^[a-zA-Z]\w*\[.*]""".toRegex()
val INTEGER_ARRAY_TEMPLATE = """\d+(?:\s\d+)*""".toRegex()
val FLOAT_ARRAY_TEMPLATE = """\d+\.\d+(?:\s\d+\.\d+)*""".toRegex()
const val VARIABLE_ATTRIBUTES_COUNT = 2
const val CALCULATION_OPERANDS_COUNT = 2


val types = mapOf<String, KClass<*>>(
    "int" to Int::class,
    "float" to Float::class,
    "int[]" to IntArray::class,
    "float[]" to FloatArray::class
)


val calculationFunctionsMap = mapOf<String, Map<List<KClass<*>>, KFunction<*>>>(
    "mod" to mapOf(
        listOf(Int::class, Int::class) to MathF.Companion::modII
    )
)

val arrayElementFunctions = mapOf<String, Map<KClass<*>, KFunction<*>>>(
    "get" to mapOf(
        Int::class to ArrayF.Companion::getI,
        Float::class to ArrayF.Companion::getF
    ),
    "set" to mapOf(
        Int::class to ArrayF.Companion::setI,
        Float::class to ArrayF.Companion::setF
    )
)

val methodsMap = mapOf(
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
    ),

    "+" to mapOf(
        listOf(Int::class, Int::class) to MathF.Companion::sumII,
        listOf(Int::class, Float::class) to MathF.Companion::sumIF,
        listOf(Float::class, Int::class) to MathF.Companion::sumFI,
        listOf(Float::class, Float::class) to MathF.Companion::sumFF,
    ),
    "-" to mapOf(
        listOf(Int::class, Int::class) to MathF.Companion::minusII,
        listOf(Int::class, Float::class) to MathF.Companion::minusIF,
        listOf(Float::class, Int::class) to MathF.Companion::minusFI,
        listOf(Float::class, Float::class) to MathF.Companion::minusFF,
    ),
    "*" to mapOf(
        listOf(Int::class, Int::class) to MathF.Companion::timeII,
        listOf(Int::class, Float::class) to MathF.Companion::timeIF,
        listOf(Float::class, Int::class) to MathF.Companion::timeFI,
        listOf(Float::class, Float::class) to MathF.Companion::timeFF,
    ),
    "/" to mapOf(
        listOf(Int::class, Int::class) to MathF.Companion::divII,
        listOf(Int::class, Float::class) to MathF.Companion::divIF,
        listOf(Float::class, Int::class) to MathF.Companion::divFI,
        listOf(Float::class, Float::class) to MathF.Companion::divFF,
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

fun parseVariableFunction(
    text: String,
    availableVariables: List<String>,
    memoryModel: MemoryModel
): Function? {
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

fun parseMethod(
    text: String,
    availableVariables: List<String>,
    memoryModel: MemoryModel,
    parseFunction: (text: String, availableVariables: List<String>, memoryModel: MemoryModel, postfixParser: PostfixExpressionParser) -> Function,
    postfixParser: PostfixExpressionParser
): Function? {
    if (!METHOD_TEMPLATE.matches(text)) return null

    val functionName = text.split('(')[0]

    if (!calculationFunctionsMap.contains(functionName)) return null

    val functionOverloads = calculationFunctionsMap.getValue(functionName)

    val parameters = text.removePrefix("$functionName(").removeSuffix(")")

    val paramFunctions = mutableListOf<Function>()

    parameters.split(getComaSeparatorIndexes(parameters)).forEach {
        paramFunctions.add(parseFunction(it, availableVariables, memoryModel, postfixParser))
    }

    val paramTypes = paramFunctions.map {
        it.type
    }

    if (!functionOverloads.containsKey(paramTypes)) return null

    val function = ReflectFunction(functionOverloads.getValue(paramTypes))
    paramFunctions.forEach {
        function.setVariableOrThrow(it)
    }
    return function
}

fun parseArrayElement(
    text: String,
    availableVariables: List<String>,
    memoryModel: MemoryModel,
    parseFunction: (text: String, availableVariables: List<String>, memoryModel: MemoryModel, postfixParser: PostfixExpressionParser) -> Function,
    postfixParser: PostfixExpressionParser
): Function? {
    if (!ARRAY_ELEMENT_TEMPLATE.matches(text)) return null

    val arrayName = text.split('[')[0]
    val arrayFunction = parseVariableFunction(arrayName, availableVariables, memoryModel)
        ?: throw Exception(SYNTAX_ERROR_TEXT)

    val index = text.removePrefix("$arrayName[").removeSuffix("]")
    val indexFunction = parseFunction(index, availableVariables, memoryModel, postfixParser)

    val getArrayElementOverloads = arrayElementFunctions["get"]

    if (getArrayElementOverloads?.containsKey(indexFunction.type) != true) return null

    val function = ReflectFunction(getArrayElementOverloads.getValue(indexFunction.type))
    function.setVariableOrThrow(arrayFunction)
    function.setVariableOrThrow(indexFunction)

    return function
}

fun parseFunction(textInput: String, availableVariables: List<String>, memoryModel: MemoryModel, postfixParser: PostfixExpressionParser): Function {
    val text = textInput.replace(" ", "")
    val stack = Stack<Function>()
    val elements = postfixParser.parseOrThrow(text)

    for (element in elements) {
        var function: Function? = null
        function = parseVariableFunction(element, availableVariables, memoryModel)

        if (function != null) {
            stack.push(function)
            continue
        }

        function = parseTypeFunction(element)

        if (function != null) {
            stack.push(function)
            continue
        }

        if (methodsMap.containsKey(element)) {
            val fOverloads = methodsMap.getValue(element)
            if (stack.count() >= 2) {
                val secondParam = stack.pop()
                val firstParam = stack.pop()

                val types = listOf(firstParam.type, secondParam.type)

                if (fOverloads.containsKey(types)) {
                    function = ReflectFunction(fOverloads[types]!!)
                    function.setVariableOrThrow(firstParam)
                    function.setVariableOrThrow(secondParam)
                    stack.push(function)
                    continue
                }
            } else {
                throw Exception(SYNTAX_ERROR_TEXT)
            }
        }

        function = parseMethod(element, availableVariables, memoryModel, ::parseFunction, postfixParser)
        if (function != null) {
            stack.push(function)
            continue
        }

        function = parseArrayElement(element, availableVariables, memoryModel, ::parseFunction, postfixParser)
        if (function != null) {
            stack.push(function)
            continue
        }
        throw Exception(SYNTAX_ERROR_TEXT)
    }

    if (stack.count() > 1 || stack.isEmpty()) {
        throw Exception(SYNTAX_ERROR_TEXT)
    }
    val resultFunction = stack.pop()

    return resultFunction
}