package com.example.educationtools.examples

import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.educationtools.R
import com.example.educationtools.base.EditorViewBase
import com.example.educationtools.blocks.NotifyBlock
import com.example.educationtools.logic.NumberCalculationFunction
import com.example.educationtools.logic.StartBlock
import com.example.educationtools.logic.TypeFunction
import com.example.educationtools.logic.Variable
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val textOne = findViewById<EditText>(R.id.textView)
        val textTwo = findViewById<EditText>(R.id.textView2)

        val editor = findViewById<EditorViewBase>(R.id.editor)
        val block = NotifyBlock()
        editor.addChild(block)
        block.apply {
            updatePosition(PointF(500f, 500f))
            updateSize(500f, 200f)
        }

        val block2 = NotifyBlock()
        editor.addChild(block2)
        block2.apply {
            updatePosition(PointF(500f, 800f))
            updateSize(500f, 200f)
        }

        val fun1 = NumberCalculationFunction.sumFunction()
        fun1.setVariableOrThrow(TypeFunction.generateTypeFunction(4))
        fun1.setVariableOrThrow(TypeFunction.generateTypeFunction(10))
        val fun2 = NumberCalculationFunction.sumFunction()
        fun2.setVariableOrThrow(fun1)
        fun2.setVariableOrThrow(fun1)
        Log.d("MainActivityMine", fun2.run().toString())


        //Logic test
        val startBlock = StartBlock()
        startBlock.updateVariables(listOf("x", "y"))
        startBlock.start(listOf(Variable(value = 10, type = "Int"), Variable(value = 5, type = "Int")))
        startBlock.work()
    }
}