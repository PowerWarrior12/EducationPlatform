package com.example.educationtools.base

import com.example.educationtools.blocks.*
import com.example.educationtools.logic.CalculationBlock
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi

class FactoryAdapter: JsonAdapter<EditableBlockFactory<EditableBlockBase>>() {

    private val moshi = Moshi.Builder().build()
    private val calculationAdapter = moshi.adapter(CalculationBlockView.Configurations::class.java)
    private val conditionAdapter = moshi.adapter(ConditionBlockView.Configurations::class.java)
    private val startAdapter = moshi.adapter(StartBlockView.Configurations::class.java)
    private val endAdapter = moshi.adapter(EndBlockView.Configurations::class.java)
    private val whileDoAdapter = moshi.adapter(WhileDoBlockView.Configurations::class.java)

    override fun fromJson(reader: JsonReader): EditableBlockFactory<EditableBlockBase>? {
        TODO("Not yet implemented")
    }

    override fun toJson(writer: JsonWriter, value: EditableBlockFactory<EditableBlockBase>?) {
        value?.also { config ->
            when(config) {
                is CalculationBlockView.Configurations -> {
                    writer.calculationAdapter
                }
            }
        }
    }
}