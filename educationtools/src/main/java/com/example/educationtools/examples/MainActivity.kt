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
import com.example.educationtools.logic.*
import java.lang.Double.sum
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.jvm.ExperimentalReflectionOnLambdas
import kotlin.reflect.jvm.reflect

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val textOne = findViewById<EditText>(R.id.textView)
        val textTwo = findViewById<EditText>(R.id.textView2)

        val editor = findViewById<EditorViewBase>(R.id.editor)

        val list = listOf<Int>(1, 2, 3, 4, 5, 6, 8)
        Log.d("Block-Shames", list.takeWhile { it > 4 }.toString())

        val memoryModel = MemoryModel()
        memoryModel.declareVarBlock("0")
        memoryModel.declareConditionBlock("1")
        memoryModel.declareVarBlock("2")
        memoryModel.declareVarBlock("3")
        memoryModel.declareDoWhileBlock("4")
        memoryModel.declareVarBlock("5")
        memoryModel.declareWhileDoBlock("6")
        memoryModel.declareVarBlock("7")
        memoryModel.declareVarBlock("8")
        memoryModel.declareWhileDoBlock("9")
        memoryModel.declareVarBlock("10")

        memoryModel.bindBlocks("0", "1")

        memoryModel.bindBlocks("1", "2")

        memoryModel.bindBlocks("2", "4")

        memoryModel.bindBlocks("4", "2")

        memoryModel.bindBlocks("5", "6")

        memoryModel.bindBlocks("6", "7")

        memoryModel.bindBlocks("7", "8")

        memoryModel.bindBlocks("8", "9")

        memoryModel.bindBlocks("9", "10")

        memoryModel.bindBlocks("10", "9")

        memoryModel.bindBlocks("9", "6")

        memoryModel.bindBlocks("4", "5")
    }
}