package com.example.educationtools.logic.parsers

import com.example.educationtools.utils.SYNTAX_ERROR_TEXT
import java.util.*

class PostfixExpressionParser {

    private val operationPriority = mapOf(
        "(" to 0,
        "||" to 1,
        "&&" to 2,
        ">" to 3,
        "<" to 3,
        "==" to 3,
        "!=" to 3,
        "<=" to 3,
        ">=" to 3,
    )

    private val operandsTwoSymbol = listOf("==","!=","<=",">=","||","&&")
    private val operandsOneSymbol = listOf('>','<')

    fun parseOrThrow(text: String): List<String> {
        var ind = 0

        fun getNumber(): String {
            var sb = StringBuilder()
            var hasDot = false
            while (ind < text.length) {
                if (text[ind].isDigit()) {
                    sb.append(text[ind])
                }
                else if (text[ind] == '.') {
                    if (hasDot) throw Exception(SYNTAX_ERROR_TEXT)
                    sb.append(text[ind])
                    hasDot = true
                } else {
                    ind--
                    break
                }
                ind++
            }
            return sb.toString()
        }

        fun getOperand(): String {
            if (ind >= text.length - 1) throw Exception(SYNTAX_ERROR_TEXT)
            val twoSymbol = "${text[ind]}${text[ind + 1]}"
            if (twoSymbol in operandsTwoSymbol) {
                ind++
                return twoSymbol
            }
            if (text[ind] in operandsOneSymbol) {
                return text[ind].toString()
            }
            throw Exception(SYNTAX_ERROR_TEXT)
        }

        fun getVariableName(): String {
            val sb = StringBuilder()
            while (ind < text.length) {
                if (LETTER_TEMPLATE.matches("${text[ind]}")) {
                    sb.append(text[ind])
                } else {
                    ind--
                    break
                }
                ind++
            }
            return sb.toString()
        }

        val stack = Stack<String>()
        val result = mutableListOf<String>()
        val startOperand = operationPriority.keys.map {
            it[0]
        }
        while (ind < text.length) {
            val c = text[ind]
            if (c.isDigit()) {
                result.add(getNumber())
            } else if (LETTER_TEMPLATE.matches("$c")) {
                result.add(getVariableName())
            } else if (c == '(') {
                stack.push(c.toString())
            } else if (c == ')') {
                while (stack.isNotEmpty() && stack.peek() != "(") {
                    result.add(stack.pop())
                }
                stack.pop()
            } else if (c in startOperand) {
                val operation = getOperand()
                while (stack.isNotEmpty() && ( operationPriority.getValue(stack.peek()) >= operationPriority.getValue(operation))) {
                    result.add(stack.pop())
                }
                stack.push(operation)
            } else {
                throw Exception(SYNTAX_ERROR_TEXT)
            }
            ind++
        }
        while (stack.isNotEmpty()) {
            result.add(stack.pop())
        }
        return result
    }
}