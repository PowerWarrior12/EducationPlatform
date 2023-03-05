package com.example.educationtools.examples

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.educationtools.R
import com.example.educationtools.base.EditorViewBase
import com.example.educationtools.logic.*
import com.example.educationtools.logic.functions.ConditionFunction
import com.example.educationtools.logic.functions.ReflectFunction
import com.example.educationtools.logic.functions.TypeFunction
import com.example.educationtools.logic.functions.VariableFunction

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val textOne = findViewById<EditText>(R.id.textView)
        val textTwo = findViewById<EditText>(R.id.textView2)

        val editor = findViewById<EditorViewBase>(R.id.editor)


        val memoryModel = MemoryModel()

        val startBlock = StartBlock(memoryModel)
        startBlock.startOrThrow(listOf(Variable("x", Int::class)))

        val calculationBlock = CalculationBlock(memoryModel)
        val function = ReflectFunction(MathF.Companion::sumII)
        function.setVariableOrThrow(VariableFunction(memoryModel, "x"))
        val minFunction = ReflectFunction(MathF.Companion::minusII)
        minFunction.setVariableOrThrow(TypeFunction.generateTypeFunction(8))
        minFunction.setVariableOrThrow(TypeFunction.generateTypeFunction(3))
        function.setVariableOrThrow(minFunction)
        calculationBlock.setFunctionAndVar(function, "x")

        val endBlock = EndBlock(memoryModel)
        endBlock.setCloseVars(listOf("x"))

        startBlock.setNextBlock(calculationBlock)
        calculationBlock.setNextBlock(endBlock)

        startBlock.startOrThrow(listOf(Variable(type = Int::class, value = 10)))
    }
}