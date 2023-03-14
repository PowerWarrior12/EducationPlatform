package com.example.educationtools.logic.parsers

import com.example.educationtools.logic.MemoryModel
import com.example.educationtools.utils.SYNTAX_ERROR_TEXT

class EndBlockParser(private val blockId: String) {
    fun parseOrThrow(text: String, memoryModel: MemoryModel): List<String> {
        val variables = text.split(',')
        val resultVariables = mutableListOf<String>()
        val availableVariables = memoryModel.getAvailableVariablesOrThrow(blockId)

        variables.forEach { variable ->
            val varWithoutSpace = variable.trim()
            if (varWithoutSpace in availableVariables) {
                resultVariables.add(varWithoutSpace)
            } else {
                throw Exception(SYNTAX_ERROR_TEXT)
            }
        }

        return resultVariables
    }
}